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

COUNTRIES = {
    'USA' 'Italy' 'France' 'New Zealand' 'Portugal' 'Spain' 'Argentina'
 'Australia' '' 'Chile' 'Romania' 'South Africa' 'Lebanon' 'Germany'
 'Hungary' 'Austria' 'UK' 'Macedonia' 'Greece'
}

FULLNESS = [
    'DRY', 'LIGHT', 'FULL', 'MEDIUM', 'SWEET', 'OFF DRY'
]

STYLES = [
    'Rich', 'Big', 'Fruity', 'Smooth', 'Rose', 'Crisp', 'Sweet', 'Dessert', '&', 'Fortified'
]

TYPES = {
    'White', 'Rose', 'Red', 'Dessert', '&', 'Fortified'
}

COL_TYPES = { #discludes ABV as needs needs % symbol removed
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

GRAPES = (
    'Chardonnay', 'Nero d\'Avola', 'Viognier', 'Sauvignon Blanc', 'Zinfandel',
    'Cabernet Sauvignon', 'Merlot', 'Pinot Noir', 'Syrah', 'Grenache', 'Riesling',
    'Malbec', 'Tempranillo', 'Sangiovese', 'Barbera', 'Shiraz', 'Pinot Grigio',
    'Chenin Blanc', 'Petit Verdot', 'Mourvedre', 'Gruner Veltliner', 'Gewurztraminer',
    'Carmenere', 'Albariño', 'Albarinho', 'Cortese', 'Fiano', 'Nebbiolo', 'Gamay', 
    'Torrontes', 'Cabernet Franc', 'Cinsault', 'MourvÃ¨dre', 'Chenin Blanc', 'Pinotage',
    'Marsanne', 'Sangiovese', 'Carignan', 'Roussanne', 'Bourboulenc', 'Clairette', 
    'SÃ©millon', 'GewÃ¼rztraminer', 'GrÃ¼ner Veltliner', 'Verdejo', 
    'Melon de Bourgogne', 'Harslevelu','Furmint', 'Bonarda', 'Grenache Blanc',
    'Palomino', 'CarmenÃ¨re', 'Rioja'
)

READ_WRONG = (
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
        self.df = pd.read_csv(FILE_NAME_IN, dtype = COL_TYPES)
        
        del self.df['Units']
        del self.df['Closure']
        del self.df['Number of Reviewers']
        del self.df['Percentage of reviewers who would buy again']
        del self.df['Mix Six Price']

        for column in [column[0] for column in list(COL_TYPES.items()) if column[1] == str]:
            self.df[column] = self.df[column].apply(lambda x: x if pd.notnull(x) else '')
        for style in ['Rose', 'White', 'Red', 'Dessert & Fortified']:
            self.df['Style'] = self.df['Style'].str.replace(style, '')        
        self.df['ABV'] = self.df['ABV'].str.replace('%', '')
        self.df['ABV'] = self.df['ABV'].astype(float)
        self.df['Awards'] = self.df['Awards'].str.replace('[', '')
        self.df['Awards'] = self.df['Awards'].str.replace('\'', '')
        self.df['Awards'] = self.df['Awards'].str.replace(']', '')
        
        self.df['ID'] = range(len(self.df))
        self.df['Year'] = pd.Series().astype(str)
        self.df.rename(columns = {'Short Description': 'Fullness', 'Per Bottle Price': 'Price'}, inplace = True) 
        
        self.rejects = pd.DataFrame().reindex_like(self.df)
        self.rejects['Reason'] = pd.Series()
        self.reject_i = 0
        self.rejected = []
                
        self.process()
    
    def reject(self, to_reject, reason):
        if to_reject.at['ID'] not in self.rejected:
            self.rejected.append(to_reject.at['ID'])
            self.rejects.loc[self.reject_i] = to_reject
            self.rejects.at[self.reject_i, 'Reason'] = reason
            self.reject_i += 1
        
    def process(self):
        self.set_grapes()
        self.find_years()
        self.validate_wines()
        
        self.rejected.reverse()
        for reject_i in self.rejected:
            self.df = self.df.drop([reject_i], axis = 0)
        
        
        
        self.df.to_csv(FILE_NAME_OUT, index = False)
        self.rejects.to_csv(FILE_NAME_REJECT_OUT, index = False)

    def validate_wines(self):
        for row in [self.df.loc[row_i] for row_i in range(len(self.df))]:
            if not self.is_country(row.at['Country']):
                self.reject(row, "country not recognised")
            elif row.at['Type'] not in TYPES:
                self.reject(row, "type not recognised")
            elif row.at['Fullness'] not in FULLNESS:
                self.reject(row, "fullness not recognised")
            elif self.df.isnull().at[row.at['ID'], 'Price']:
                self.reject(row, "no price listed")
            elif self.is_number(row.at["Long Description"]):
                self.reject(row, "Long description malformed")
            elif any([not self.is_number(string) for string in 
                      [row.at[field] for field in ['Price', 'ABV', 'Volume']]]):
                self.reject(row, "non-number contained in Price, ABV or Volume entry")
            if not self.is_number(row.at['Price']):
                print(row.at['Price'])
                
    def is_number(self, string):
        try:
            float(string)
            return True
        except ValueError:
            return False
        
    def is_country(self, country):
        try:
            pycountry.countries.search_fuzzy(country)
            return True
        except LookupError:
            return False      
               
    def find_years(self):
        for row_i in range(len(self.df)):
            for year_cand in range(START_YEAR, END_YEAR+1):
                year_cand = str(year_cand)
                if year_cand in self.df.at[row_i, 'Name']:
                    self.df.at[row_i, 'Year'] = year_cand
            if pd.isnull(self.df).at[row_i, 'Year']:
                self.df.at[row_i, 'Year'] = 'Unknown'
        if REMOVE_YEAR_FROM_NAME:
            for year_cand in range(START_YEAR, END_YEAR+1):
                self.df['Name'] =  self.df['Name'].str.replace(str(year_cand), '')
    
    def set_grapes(self):
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
    
    
    