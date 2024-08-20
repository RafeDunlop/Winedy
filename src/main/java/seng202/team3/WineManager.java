package seng202.team3;


import seng202.team3.io.Importable;
import seng202.team3.models.Wine;
import seng202.team3.services.WineDAO;

import java.io.File;
import java.util.List;


/**
 * Class to handle all actions for sales. This acts as a MVC controller taking requests from the view and completing
 * these using relevant model/repository layer actions
 * @author Morgan English
 */
public class WineManager {
    private final WineDAO wineDAO;

    /**
     * Creates a new SalesManager object and creates a private SaleDAO object it will later use for all database
     * interactions
     */
    public WineManager() {
        wineDAO = new WineDAO();
    }

    /**
     * Saves a file of sales to the repository layer using the specified importer functionality
     * TODO: handle errors gracefully
     * @param importer importer object to use
     * @param file file to be imported
     */
    public void addAllWinesFromFile(Importable<Wine> importer, File file) {
        List<Wine> wines = importer.readFromFile(file);
        int i = 0;
        while (i < wines.size()) {
            if (i + 100 > wines.size()) {
                wineDAO.addBatch(wines.subList(i, wines.size()));
            } else {
                wineDAO.addBatch(wines.subList(i, i + 100));
            }
            i += 100;
        }
    }

    /**
     * Adds a wine
     * @param wine wine to add
     * @return true if sale added without error
     */
    public int addWine(Wine wine) {
        return wineDAO.add(wine);
    }

//    /**
//     * Takes in base components of a sale (specifically an address field as text) and creates a new sale object
//     * using geolocation of address
//     * @param geolocationManager GeolocationManager implementation to use for geolocation of address
//     * @param item sale item
//     * @param price sale price
//     * @param buyerFName buyer first name
//     * @param buyerLName buyer last name
//     * @param address address of sale, to be geolocated
//     * @return true if successful, false if failed
//     */
//    public boolean createAndAddSaleWithAddress(GeolocationManager geolocationManager, String item, float price, String buyerFName, String buyerLName, String address) {
//        GeoLocationResult locationResult = geolocationManager.queryAddress(address);
//        Sale sale = new Sale(item, price, buyerFName, buyerLName, locationResult.getLat(), locationResult.getLng());
//        int id = addSale(sale);
//        if (id != -1){
//            sale.setId(id);
//            return true;
//        }
//        return false;
//    }

    /**
     * Deletes a sale
     * todo work out how to get id for deletion, add id to sale model? add fetch id to saledao?
     * @param wine wine to delete
     * @return true iff deleted, else false (what if it never existed?)
     */
    public boolean deleteWine(Wine wine) {
        wineDAO.delete(wine.getUniqueWineID());
        return false;
    }

    /**
     * Gets all wines from repository layer
     * @return List of all wines
     */
    public List<Wine> getAllWines() {
        return wineDAO.getAll();
    }

    /**
     * Gets sale from persistence by id
     * @param id id of wine to fetch
     * @return wine specified by id or null if it doesn't exist
     */
    public Wine getWineById(int id) {
        return wineDAO.getOne(id);
    }
}
