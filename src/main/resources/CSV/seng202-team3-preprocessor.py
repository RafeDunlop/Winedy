"""
@author Rafe Dunlop (rdu46)
SENG202 2024
Preprocessor for CSV file

uses standard input/output to convert a CSV file from one specified format to another
"""

import sys
import pandas as pd
import pycountry

START_YEAR = 1900
END_YEAR = 2025
GET_ACCEPT = True
FILE_NAME_IN = "majestic_df.csv"
FILE_NAME_OUT = "majestic_df_preprocessed.csv"
FILE_NAME_REJECT_OUT = "majestic_rejects.csv"

GRAPES = {
    'Chardonnay', 'Nero d\'Avola', 'Viognier', 'Sauvignon Blanc', 'Zinfandel',
    'Cabernet Sauvignon', 'Merlot', 'Pinot Noir', 'Syrah', 'Grenache', 'Riesling',
    'Malbec', 'Tempranillo', 'Sangiovese', 'Barbera', 'Shiraz', 'Pinot Grigio',
    'Chenin Blanc', 'Petit Verdot', 'Mourvedre', 'Gruner Veltliner', 'Gewurztraminer',
    'Carmenere', 'Albariño', 'Cortese', 'Fiano', 'Nebbiolo', 'Gamay', 'Torrontes'
}

FULLNESS = {
    'DRY', 'LIGHT', 'FULL', 'MEDIUM', 'SWEET', 'OFF DRY'
}

STYLES = {
    'Rich', 'Big', 'Fruity', 'Smooth', 'Rose', 'Crisp', 'Sweet', 'Dessert', '&', 'Fortified'
}




class Preprocessor:
    """
    converts a csv in the format of majestic_df.csv into the required format,
    featuring a year column and a 
    """
    def __init__(self):
        """takes a file object and turns it into a list for the header and a 
        list of lists representing all body lines
        initializes id to 0
        """
        self.df = pd.read_csv(FILE_NAME_IN)
        del self.df['Units']
        del self.df['Closure']
        del self.df['Number of Reviewers']
        del self.df['Percentage of reviewers who would buy again']
        del self.df['Mix Six Price']
        self.df['ID'] = range(len(self.df))
        self.df['Year'] = pd.Series()
        self.df.rename(columns = {'Short Description': 'Fullness'})
        self.rejects = pd.DataFrame().reindex_like(self.df)
        self.reject_i = 0
        
    def process(self):
        for line_i in range(len(self.df)):
            self.get_missing(self.df.loc[line_i])
            self.df.loc[line_i]['Awards'].strip('[]\'')
            if not self.remove_percent(self.df.loc[line_i]):
                self.reject(line_i)
            elif not self.restructure_style(self.df.loc[line_i]):
                self.reject(line_i)
            elif not self.validate_wine(self.df.loc[line_i]):
                self.reject(line_i)
                
            self.df.to_csv(FILE_NAME_OUT, index = False)
            self.rejects.to_csv(FILE_NAME_REJECT_OUT, index = False)
                
    def reject(self, to_reject):
        self.rejects.loc[self.reject_i] = self.df.loc[to_reject]
        self.df.drop(labels = to_reject, inplace = True)
        self.reject_i += 1
    
    def remove_percent(self, row):
        row['ABV'].strip('%')
        try:
            num = int(row['ABV'])
            return True
        except:
            return False
    
    def restructure_style(self, row):
        wine_type = row['Type']
        valids = ''
        for word in row['Style'].split():
            if word == wine_type:
                pass
            elif word in STYLES:
                valids += word + ' '
            else: 
                return False
        row['Style'] = valids[:-1]
        return True

    def validate_wine(self, row):
        if pycountry.countries.search_fuzzy(row['Country']) == None:
            return False
        elif row['Grape'] == None:
            return False
        elif row['Fullness'] not in FULLNESS:
            return False
        else:
            try:        
                num = int(row['Per Bottle Price'])
            except:
                return False
        
        
    def get_missing(self, row):
        words = row['Name'].split()
        has_grape = row['Grape'] != None
        for word in words:
            
            year_cand = self.get_year(word)
            if year_cand != None:
                row['Year'] = year_cand
                
            if not has_grape:
                if word in GRAPES:
                    row['Grapes'] += word
             
    def get_year(self, string):
        seen_num = False
        numstring = ''
        for char in string:
            try:
                num = int(char)
                seen_num = True
                numstring += str(num)
            except:
                if seen_num:
                    if self.validate_year(int(numstring)):
                        return numstring
                    else:
                        seen_num = False
        return None
    
    def validate_year(self, year):
        """returns true if year is between start year and end year, or otherwise false"""
        return (START_YEAR < year < END_YEAR)
    

if __name__ == '__main__':
    preprocessor = Preprocessor()
    preprocessor.process()
    