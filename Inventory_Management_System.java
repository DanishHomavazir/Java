import java.util.*;

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
        return "ID: " + id + " | Company: "+ companyName + "| Name: " + name + " - Rs: " + price + " (Discount: "+ (discount * 100)+"%) (Stock: " + stockQuantity + ")";
    }
}

// Exception class for handling invalid input
class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

class InventoryManager {
    private static Item[] items = new Item[15]; 

    static {
        items[0] = new Item(111, "Lenovo", "Laptop", 75000, 10);
        items[1] = new Item(124, "Logitech", "Mouse", 500, 50);
        items[2] = new Item(412, "Keychron", "Keyboard", 1500, 30);
        items[3] = new DiscountedItem(475, "Acer", "Monitor", 15000, 20, 0.1);
        items[4] = new Item(506, "HP", "Printer", 9500, 15);
    }

    public static void displayInventory() {
        System.out.println("Current Inventory:");
        System.out.println("+-----------------------------------------------------------------------------------------------------------+");
        System.out.println("| ID  | Company                            | Name        | Price          | Stock Quantity  | Discount      |");
        System.out.println("+-----------------------------------------------------------------------------------------------------------+");
        for (Item item : items) {
            if (item != null) {
                if (item instanceof DiscountedItem) {
                    System.out.printf("| %-3d | %-35s| %-12s| Rs: %-8.1f   | %-16d |%-12.2f%% |\n", item.getId(), item.companyName, item.name, item.price, item.stockQuantity,((DiscountedItem) item).getDiscount()*100);
                } else {
                    System.out.printf("| %-3d | %-35s| %-12s| Rs: %-8.1f   | %-16d | N/A          |\n", item.getId(), item.companyName, item.name, item.price, item.stockQuantity);
                }
            }
        }
        System.out.println("+-----------------------------------------------------------------------------------------------------------+");
    }


    // Method to add an item to inventory
    public static void addItem(int id, String companyName, String name, double price, int stockQuantity) throws InvalidInputException {
        // Check if the item ID already exists
        for (Item item : items) {
            if (item != null && item.getId() == id) {
                throw new InvalidInputException("Item with the ID: " + id + " already exists please enter a different ID for the new item.");
            }
        }
        
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = new Item(id, companyName, name, price, stockQuantity);
                System.out.println("Item added to inventory: " + name);
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
                return;
            }
        }
        throw new InvalidInputException("Item not found in inventory: ID " + id);
    }

    // Method to search for an item by ID
    public static void searchItem(int id) {
        boolean found = false;
        for (Item item : items) {
            if (item != null && item.getId() == id) {
                System.out.println("Item found in inventory: " + item);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Item not found in inventory: ID " + id);
        }
    }

    // Method to edit an item's price
    public static void editItem(int id, double newPrice) throws InvalidInputException {
        for (Item item : items) {
            if (item != null && item.getId() == id) {
                item.price = newPrice;
                System.out.println("Item price updated: " + item);
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
                return;
            }
        }
        throw new InvalidInputException("Item not found in inventory: ID " + id);
    }
}

public class Inventory_Management_System{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        int itemId;
        String companyName;
        String itemName;
        double itemPrice;
        int stockQuantity;

        do {
            System.out.println("\n---------------Menu:---------------");
            System.out.println("1. Add item to inventory");
            System.out.println("2. Remove item from inventory");
            System.out.println("3. Search for item by ID");
            System.out.println("4. Edit item price");
            System.out.println("5. Add stock quantity for item");
            System.out.println("6. Remove stock quantity for item");
            System.out.println("7. Show available items in inventory");
            System.out.println("8. Exit");
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
                    System.out.print("Enter stock quantity: ");
                    stockQuantity = scanner.nextInt();
                    try {
                        InventoryManager.addItem(itemId, companyName, itemName, itemPrice, stockQuantity);
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
                    System.out.print("Enter item ID to search for the item in the inventory: ");
                    itemId = scanner.nextInt();
                    scanner.nextLine();
                    InventoryManager.searchItem(itemId);
                    break;
                case 4:
                    System.out.print("Enter item ID to edit the its price: ");
                    itemId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new price: ");
                    itemPrice = scanner.nextDouble();
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
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
            }
        } while (choice != 8);
        scanner.close();
    }
}
