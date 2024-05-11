import java.util.*;
import java.io.*;

interface Sellable {
    double calculatePrice();
}

class Item implements Sellable {
    protected int id;
    protected String name;
    protected double price;
    protected int stockQuantity;
    protected String companyName;

    public Item(int id, String companyName, String name, double price, int stockQuantity) {
        this.id = id;
        this.companyName = companyName;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    @Override
    public double calculatePrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Company: " + companyName + " | Name: " + name + " - Rs: " + price + " (Stock: " + stockQuantity + ")";
    }

    // Method to add stock quantity
    public void addStock(int quantity) {
        stockQuantity += quantity;
    }

    // Method to remove stock quantity
    public void removeStock(int quantity) throws InvalidInputException {
        if (stockQuantity >= quantity) {
            stockQuantity -= quantity;
        } else {
            throw new InvalidInputException("Insufficient stock for item: " + name);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class DiscountedItem extends Item {
    private double discount;

    public DiscountedItem(int id, String companyName, String name, double price, int stockQuantity, double discount) {
        super(id, companyName, name, price, stockQuantity);
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    @Override
    public double calculatePrice() {
        return price * (1 - discount);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Company: " + companyName + " | Name: " + name + " - Rs: " + price + " (Discount: "+ (discount * 100)+"%) (Stock: " + stockQuantity + ")";
    }
}

class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

class InventoryManager {
    private static final String FILENAME = "inventory.txt";
    public static Item[] items = new Item[15];

    static {
        try {
            loadInventoryFromFile();
        } catch (IOException e) {
            System.out.println("Error loading inventory from file: " + e.getMessage());
        }
    }

    public static void displayInventory() {
        System.out.println("Current Inventory:");
        System.out.println("+--------------------------------------------------------------------------------------------------------------------------+");
        System.out.println("| ID  | Company                            | Name                 | Price           | Stock Quantity   | Discount          |");
        System.out.println("+--------------------------------------------------------------------------------------------------------------------------+");
        for (Item item : items) {
            if (item != null) {
                if (item instanceof DiscountedItem) {
                    System.out.printf("| %-3d | %-35s| %-20s | Rs: %-8.1f    | %-16d |%-12.2f%%      |\n", item.getId(), item.companyName, item.name, item.price, item.stockQuantity, ((DiscountedItem) item).getDiscount()*100);
                } else {
                    System.out.printf("| %-3d | %-35s| %-20s | Rs: %-8.1f    | %-16d | N/A               |\n", item.getId(), item.companyName, item.name, item.price, item.stockQuantity);
                }
            }
        }
        System.out.println("+--------------------------------------------------------------------------------------------------------------------------+");
    }

    // Method to add an item to inventory
    public static void addItem(int id, String companyName, String name, double price, int stockQuantity, double discount) throws InvalidInputException {
        // Check if the item ID already exists
        for (Item item : items) {
            if (item != null && item.getId() == id) {
                throw new InvalidInputException("Item with the ID: " + id + " already exists please enter a different ID for the new item.");
            }
        }

        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                if (discount > 0) {
                    items[i] = new DiscountedItem(id, companyName, name, price, stockQuantity, discount);
                } else {
                    items[i] = new Item(id, companyName, name, price, stockQuantity);
                }
                System.out.println("Item added to inventory: " + name);
                try {
                    saveInventoryToFile();
                } catch (IOException e) {
                    System.out.println("Error saving inventory to file: " + e.getMessage());
                }
                return;
            }
        }
        System.out.println("Inventory is full. Cannot add more items.");
    }

    // Method to remove an item from inventory
    public static void removeItem(int id) throws InvalidInputException {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getId() == id) {
                items[i] = null;
                System.out.println("Item removed from inventory: ID " + id);
                try {
                    saveInventoryToFile();
                } catch (IOException e) {
                    System.out.println("Error saving inventory to file: " + e.getMessage());
                }
                return;
            }
        }
        throw new InvalidInputException("Item not found in inventory: ID " + id);
    }

    // Method to search for an item
    public static void searchItem(String name) {
        boolean found = false;
        for (Item item : items) {
            if (item != null && item.getName().equalsIgnoreCase(name)) {
                System.out.println("Item found in inventory: " + item);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Item not found in inventory: " + name);
        }
    }

    // Method to edit an item's price
    public static void editItem(int id, double newPrice) throws InvalidInputException {
        for (Item item : items) {
            if (item != null && item.getId() == id) {
                item.price = newPrice;
                System.out.println("Item price updated: " + item);
                try {
                    saveInventoryToFile();
                } catch (IOException e) {
                    System.out.println("Error saving inventory to file: " + e.getMessage());
                }
                return;
            }
        }
        throw new InvalidInputException("Item not found in inventory: ID " + id);
    }

    // Method to add stock quantity for an item
    public static void addStock(int id, int quantity) throws InvalidInputException {
        for (Item item : items) {
            if (item != null && item.getId() == id) {
                item.addStock(quantity);
                System.out.println("Stock added for item: " + item);
                try {
                    saveInventoryToFile();
                } catch (IOException e) {
                    System.out.println("Error saving inventory to file: " + e.getMessage());
                }
                return;
            }
        }
        throw new InvalidInputException("Item not found in inventory: ID " + id);
    }

    // Method to remove stock quantity for an item
    public static void removeStock(int id, int quantity) throws InvalidInputException {
        for (Item item : items) {
            if (item != null && item.getId() == id) {
                item.removeStock(quantity);
                System.out.println("Stock removed for item: " + item);
                try {
                    saveInventoryToFile();
                } catch (IOException e) {
                    System.out.println("Error saving inventory to file: " + e.getMessage());
                }
                return;
            }
        }
        throw new InvalidInputException("Item not found in inventory: ID " + id);
    }

 // Method to request an item from inventory
public static void requestItem(String name, int quantity) {
    boolean found = false;
    for (Item item : items) {
        if (item != null && item.getName().equalsIgnoreCase(name)) {
            found = true;
            if (item.stockQuantity >= quantity) {
                item.stockQuantity -= quantity;
                System.out.println("Item successfully purchased: " + item.getName());
                try {
                    saveInventoryToFile();
                } catch (IOException e) {
                    System.out.println("Error saving inventory to file: " + e.getMessage());
                }
            } else {
                System.out.println("Insufficient stock for item: " + item.getName());
            }
            break;
        }
    }
    if (!found) {
        System.out.println("Item not found in inventory: " + name);
    }
}

// Method to load inventory from file
private static void loadInventoryFromFile() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
    String line;
    int index = 0;

    while ((line = reader.readLine()) != null) {
        if (line.contains("Current Inventory:")) {
            break;
        }
    }

    reader.readLine();

    while ((line = reader.readLine()) != null) {
        if (line.startsWith("+----+")) {
            continue; 
        }
        String[] parts = line.split("\\|");
        if (parts.length > 1) {
            int id = Integer.parseInt(parts[1].trim());
            String companyName = parts[2].trim();
            String name = parts[3].trim();
            double price = Double.parseDouble(parts[4].trim());
            int stockQuantity = Integer.parseInt(parts[5].trim());
            double discount = 0.0;
            if (!parts[6].trim().equals("N/A")) {
                discount = Double.parseDouble(parts[6].trim());
            }
            if (parts.length == 7) {
                items[index++] = new DiscountedItem(id, companyName, name, price, stockQuantity, discount);
            } else {
                items[index++] = new Item(id, companyName, name, price, stockQuantity);
            }
        }
    }
    reader.close();
}

    // Method to save inventory to file
    private static void saveInventoryToFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME));
        writer.write("Current Inventory:- \n");
        writer.write("+----+----------------------------------+--------------------------+------------+-----------------+--------------+\n"); 
        writer.write("| ID | Company                          | Name                     | Price      | Stock Quantity  | Discount     |\n"); 
        writer.write("+----+----------------------------------+--------------------------+------------+-----------------+--------------+\n"); 
        for (Item item : items) {
            if (item != null) {
                if (item instanceof DiscountedItem) {
                    DiscountedItem discountedItem = (DiscountedItem) item;
                    writer.write(String.format("| %-3d| %-33s| %-20s     | %-10.1f | %-15d | %-8.1f%%    |\n", item.getId(), item.companyName, item.name, item.price, item.stockQuantity, discountedItem.getDiscount() * 100));
                } else {
                    writer.write(String.format("| %-3d| %-33s| %-20s     | %-10.1f | %-15d | N/A          |\n", item.getId(), item.companyName, item.name, item.price, item.stockQuantity));
                }
                writer.write("+----+----------------------------------+--------------------------+------------+-----------------+--------------+\n"); 
            }
        }
        writer.close();
    }
}

public class File_Handling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        int itemId;
        String companyName;
        String itemName;
        double itemPrice;
        int stockQuantity;
        double discount;

        do {
            System.out.println("\n---------------Menu:---------------");
            System.out.println("1. Add item to inventory");
            System.out.println("2. Remove item from inventory");
            System.out.println("3. Search for item by name");
            System.out.println("4. Edit item price");
            System.out.println("5. Add stock quantity for item");
            System.out.println("6. Remove stock quantity for item");
            System.out.println("7. Show available items in inventory");
            System.out.println("8. Request item from inventory");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter item ID to add the item(Item ID must be unique): ");
                    itemId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter company name: ");
                    companyName = scanner.nextLine();
                    System.out.print("Enter item name: ");
                    itemName = scanner.nextLine();
                    System.out.print("Enter item price: ");
                    itemPrice = scanner.nextDouble();
                    scanner.nextLine(); 
                    System.out.print("Enter stock quantity: ");
                    stockQuantity = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Discount percentage (in decimal value) (if no discount, enter 0): ");
                    discount = scanner.nextDouble();
                    scanner.nextLine(); 
                    try {
                        InventoryManager.addItem(itemId, companyName, itemName, itemPrice, stockQuantity, discount);
                    } catch (InvalidInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Enter item ID to remove the item: ");
                    itemId = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        InventoryManager.removeItem(itemId);
                    } catch (InvalidInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Enter item name to search for the item in the inventory: ");
                    itemName = scanner.nextLine();
                    InventoryManager.searchItem(itemName);
                    break;
                case 4:
                    System.out.print("Enter item ID to edit the its price: ");
                    itemId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new price: ");
                    itemPrice = scanner.nextDouble();
                    scanner.nextLine(); 
                    try {
                        InventoryManager.editItem(itemId, itemPrice);
                    } catch (InvalidInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.print("Enter item ID to add stock quantity: ");
                    itemId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter quantity to add: ");
                    stockQuantity = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        InventoryManager.addStock(itemId, stockQuantity);
                    } catch (InvalidInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    System.out.print("Enter item ID to remove stock quantity: ");
                    itemId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter quantity to remove: ");
                    stockQuantity = scanner.nextInt();
                    scanner.nextLine(); 
                    try {
                        InventoryManager.removeStock(itemId, stockQuantity);
                    } catch (InvalidInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 7:
                    InventoryManager.displayInventory();
                    break;
                case 8:
                    System.out.print("Enter item name to request from inventory: ");
                    String itemNameToRequest = scanner.nextLine();
                    boolean itemFound = false;
                    for (Item item : InventoryManager.items) {
                        if (item != null && item.name.equalsIgnoreCase(itemNameToRequest)) {
                            itemFound = true;
                            System.out.println("Item available in the inventory.");
                            System.out.print("Enter quantity to purchase: ");
                            int quantityToPurchase = scanner.nextInt();
                            scanner.nextLine(); 
                            try {
                                InventoryManager.removeStock(item.getId(), quantityToPurchase);
                                System.out.println("Item successfully purchased.");
                            } catch (InvalidInputException e) {
                                System.out.println("Error purchasing item: " + e.getMessage());
                            }
                            break;
                        }
                    }
                    if (!itemFound) {
                        System.out.println("Item not available in the inventory.");
                    }
                    break;
                
                
                case 9:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
            }
        } while (choice != 9);
        scanner.close();
    }
}
