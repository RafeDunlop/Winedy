"""
@author Rafe Dunlop (rdu46)
SENG202 2024
Preprocessor for CSV file

outputs two csv files, one for rejected wines, one for the preprocessed wines
"""

import pandas as pd
import pycountry

START_YEAR = 1900
END_YEAR = 2025
GET_ACCEPT = True
FILE_NAME_IN = "majestic_df.csv"
FILE_NAME_OUT = "majestic_df_preprocessed.csv"
FILE_NAME_REJECT_OUT = "majestic_rejects.csv"
REMOVE_YEAR_FROM_NAME = False
REMOVE_GRAPE_FROM_NAME = False

COUNTRIES = { #set of all valid countries that appear in the database obtained via pandas
    'USA', 'Italy', 'France', 'New Zealand', 'Portugal', 'Spain', 'Argentina',
 'Australia', 'Chile', 'Romania', 'South Africa', 'Lebanon', 'Germany',
 'Hungary', 'Austria', 'UK', 'Macedonia', 'Greece'
}

FULLNESS = { #set of all valid fullnesses in the database obtained via pandas
    'DRY', 'LIGHT', 'FULL', 'MEDIUM', 'SWEET', 'OFF DRY'
}

STYLES = ( #list of valid style words. Iterated through, so left as a list (not set)
    'Rich', 'Big', 'Fruity', 'Smooth', 'Rose', 'Crisp', 'Sweet', 'Dessert', '&', 'Fortified'
)

COLOURS = { #set of valid 'words' that can appear in the colours column 
    'White', 'Rose', 'Red', 'Dessert', '&', 'Fortified'
}

COL_TYPES = { #discludes ABV as needs needs % symbol removed first
    'Name': str,
    'Country': str,
    'Type': str,
    'Style': str,
    'Grape': str,
    'Short Description': str,
    'Long Description': str,
    'Per Bottle Price': float,
    'Awards': str,
    'Volume': float
}

GRAPES = ( #this tuple is manually constructed and is the main source of potential of defects
    'Chardonnay', 'Nero d\'Avola', 'Viognier', 'Sauvignon Blanc', 'Zinfandel',
    'Cabernet Sauvignon', 'Merlot', 'Pinot Noir', 'Syrah', 'Grenache', 'Riesling',
    'Malbec', 'Tempranillo', 'Sangiovese', 'Barbera', 'Shiraz', 'Pinot Grigio',
    'Chenin Blanc', 'Petit Verdot', 'Mourvedre', 'Gruner Veltliner', 'Gewurztraminer',
    'Carmenere', 'Albariño', 'Albarinho', 'Cortese', 'Fiano', 'Nebbiolo', 'Gamay', 
    'Torrontes', 'Cabernet Franc', 'Cinsault', 'MourvÃ¨dre', 'Pinotage',
    'Marsanne', 'Carignan', 'Roussanne', 'Bourboulenc', 'Clairette', 
    'SÃ©millon', 'GewÃ¼rztraminer', 'GrÃ¼ner Veltliner', 'Verdejo', 
    'Melon de Bourgogne', 'Harslevelu','Furmint', 'Bonarda', 'Grenache Blanc',
    'Palomino', 'CarmenÃ¨re', 'Rioja'
)

READ_WRONG = ( #this pseudo-dictionary accounts for encoding errors in the grapes table
    ('MourvÃ¨dre', 'Mourvèdre'),
    ('SÃ©millon', 'Semillon'),
    ('GewÃ¼rztraminer', 'Gewürztraminer'),
    ('AlbariÃ±o', 'Albariño'),
    ('Albarinho', 'Albariño'),
    ('GrÃ¼ner Veltliner', 'Grüner Veltliner'),
    ('CarmenÃ¨re', 'Carménère')
)

GRAPE_DICT = dict()

def init_grape_dict(grape_dict):
    """
    function for initializing GRAPE_DICT using READ_WRONG 
    """
    for grape in GRAPES:
        grape_dict[grape] = grape
    for wrong, right in READ_WRONG:
        grape_dict[wrong] = right

class Preprocessor:
    """
    converts a csv in the format of majestic_df.csv into the required format,
    featuring a year column and an ID column
    
    also creates a file for rejects with a column specifying the reason for the error
    """
    def __init__(self):
        """takes a file object and turns it into a list for the header and a 
        list of lists representing all body lines
        initializes id to 0
        """
        self.df = pd.read_csv(FILE_NAME_IN, dtype = COL_TYPES) #read in FILE_NAME_IN, specifying column types
        
        #delete unused columns
        del self.df['Units']
        del self.df['Closure']
        del self.df['Number of Reviewers']
        del self.df['Percentage of reviewers who would buy again']
        del self.df['Mix Six Price']

        #make empty string fields ''
        for column in [column[0] for column in list(COL_TYPES.items()) if column[1] == str]:
            self.df[column] = self.df[column].apply(lambda x: x if pd.notnull(x) else '')
        
        #make style not repeat colour
        for colour in COLOURS:
            self.df['Style'] = self.df['Style'].str.replace(colour, '')
        
        #remove '%' character from ABV so it can be interpreted as a float 
        self.df['ABV'] = self.df['ABV'].str.replace('%', '')
        self.df['ABV'] = self.df['ABV'].astype(float)
        
        #remove escape characters and list opener and closer
        self.df['Awards'] = self.df['Awards'].str.replace('[', '')
        self.df['Awards'] = self.df['Awards'].str.replace('\'', '')
        self.df['Awards'] = self.df['Awards'].str.replace(']', '')
        
        #create id field
        self.df['ID'] = range(len(self.df))
        
        #create Year field (to be filled)
        self.df['Year'] = pd.Series().astype(str)
        
        #rename columns whose names does not reflect our usage
        self.df.rename(columns = {'Short Description': 'Fullness', 
                                  'Per Bottle Price': 'Price', 
                                  'Type': 'Colour'}, inplace = True) 
        
        #initialize rejects df
        self.rejects = pd.DataFrame().reindex_like(self.df)
        self.rejects['Reason'] = pd.Series()
        self.reject_i = 0
        self.rejected = []
                
        self.process()
    
    def reject(self, to_reject, reason):
        """
        adds the given row to a list to be removed from the output
        copies the row into the rejects df with  parameter reason in the reason column 
        """
        if to_reject.at['ID'] not in self.rejected: #ensure no double ups
            self.rejected.append(to_reject.at['ID'])
            self.rejects.loc[self.reject_i] = to_reject
            self.rejects.at[self.reject_i, 'Reason'] = reason
            self.reject_i += 1
        
    def process(self):
        """
        calls methods which process the grapes, years and validates the unrejected wines
        removes rejected wines from the output
        outputs the output csvs
        """
        self.set_grapes()
        self.find_years()
        self.validate_wines()
        
        self.rejected.reverse() #reversed to ensure id integrity whilst iterating
        for reject_i in self.rejected:
            self.df = self.df.drop([reject_i], axis = 0)
        
        self.df.to_csv(FILE_NAME_OUT, index = False)
        self.rejects.to_csv(FILE_NAME_REJECT_OUT, index = False)

    def validate_wines(self):
        """
        ensures that column values conform to constraints using helper functions
        """
        for row in [self.df.loc[row_i] for row_i in range(len(self.df))]:
            if not self.is_country(row.at['Country']):
                self.reject(row, "country not recognised")
            elif row.at['Colour'] not in COLOURS:
                self.reject(row, "colour not recognised")
            elif row.at['Fullness'] not in FULLNESS:
                self.reject(row, "fullness not recognised")
            elif self.df.isnull().at[row.at['ID'], 'Price']:
                self.reject(row, "no price listed")
            elif self.is_number(row.at["Long Description"]):
                self.reject(row, "Long description malformed")
                
            # ensure that all number fields contain a number (except year - can have "Unknown")
            elif any([not self.is_number(string) for string in 
                      [row.at[field] for field in ['Price', 'ABV', 'Volume']]]):
                self.reject(row, "non-number contained in Price, ABV or Volume entry")
            if not self.is_number(row.at['Price']):
                print(row.at['Price'])
                
    def is_number(self, string):
        """
        wrapper to try block which determines if string represents a number
        """
        try:
            float(string)
            return True
        except ValueError:
            return False
        
    def is_country(self, country):
        """
        wrapper to try block using pycountry which determines if country represents
        a country or territory recognised by pycountry
        
        used to generate COUNTRIES
        """
        try:
            pycountry.countries.search_fuzzy(country)
            return True
        except LookupError:
            return False      
               
    def find_years(self):
        """
        expensive operation which determines if year is in the wines name by checking every year until one is present
        or all are exhausted, in which case the year is "unknown"
        if REMOVE_YEAR_FROM_NAME is true, removes the year from the name
        """
        for row_i in range(len(self.df)):
            for year_cand in range(START_YEAR, END_YEAR+1):
                year_cand = str(year_cand)
                if year_cand in self.df.at[row_i, 'Name']:
                    self.df.at[row_i, 'Year'] = year_cand
                    break
            if pd.isnull(self.df).at[row_i, 'Year']:
                self.df.at[row_i, 'Year'] = 'Unknown'
        if REMOVE_YEAR_FROM_NAME:
            for year_cand in range(START_YEAR, END_YEAR+1):
                self.df['Name'] =  self.df['Name'].str.replace(str(year_cand), '')
    
    def set_grapes(self):
        """
        overwrites the grape column by checking each grape recognised by the database against the name and grape
        this way it is assured that duplicates may not occur (unless duplicated in GRAPES)
        wines with no recognised grape are rejected
        if REMOVE_GRAPE_FROM_NAME is true, all recognised grapes are removed from the name column
        """
        for row_i in range(len(self.df)):
            first = True
            new = None
            for grape in GRAPES:
                if grape in self.df.at[row_i, 'Name'] or grape in self.df.at[row_i, 'Grape']:
                    if first:
                        first = False
                        new = GRAPE_DICT[grape]
                    else:
                        new += ', ' + GRAPE_DICT[grape]
                    
            self.df.at[row_i, 'Grape'] = new
            if new == None:
                self.reject(self.df.loc[row_i], "no grapes could be parsed")
        if REMOVE_GRAPE_FROM_NAME:
            for grape in GRAPES:
                self.df['Name'] =  self.df['Name'].str.replace(grape, '')        
            
                
if __name__ == '__main__':
    init_grape_dict(GRAPE_DICT)
    preprocessor = Preprocessor()
    
    
    