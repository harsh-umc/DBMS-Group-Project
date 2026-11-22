
// Farmers Markets
const markets = [
    ["Downtown Farmers Market", "123 Main St"],
    ["Riverside Farmers Market", "456 River Rd"],
    ["Green Valley Market", "789 Valley Dr"],
    ["Sunrise Farmers Market", "101 Sunrise Ave"],
    ["Oakwood Farmers Market", "202 Oak St"],
    ["Lakeside Farmers Market", "303 Lake Rd"],
    ["Harvest Hill Market", "404 Hill St"],
    ["Prairie Farmers Market", "505 Prairie Ave"],
    ["Maple Grove Market", "606 Maple Dr"],
    ["Fresh Fields Market", "707 Field Rd"]
];

// Vendors
const vendors = [
    ["Green Acres", "greenacres.com"],
    ["Sunny Farms", "sunnyfarms.com"],
    ["Oak Hill Farm", "oakhillfarm.com"],
    ["Fresh Harvest", "freshharvest.com"],
    ["Riverbend Farm", "riverbendfarm.com"],
    ["Blue Sky Farm", "blueskyfarm.com"],
    ["Golden Fields", "goldenfields.com"],
    ["Maple Creek", "maplecreek.com"],
    ["Happy Hen Farm", "happyhenfarm.com"],
    ["Prairie Roots", "prairieroots.com"],
    ["Red Barn Farm", "redbarnfarm.com"],
    ["Hilltop Produce", "hilltopproduce.com"],
    ["Valley Fresh", "valleyfresh.com"],
    ["Morning Dew", "morningdew.com"],
    ["Cedar Grove", "cedargrove.com"],
    ["Sweet Berry", "sweetberry.com"],
    ["Meadow Farms", "meadowfarms.com"],
    ["Farmhouse Eggs", "farmhouseeggs.com"],
    ["Country Greens", "countrygreens.com"],
    ["Wildflower Farm", "wildflowerfarm.com"]
];

// Products
const products = [
    [0, "Apple", "Produce"],
    [1, "Orange", "Produce"],
    [2, "Pear", "Produce"],
    [3, "Watermelon", "Produce"],
    [4, "Pumpkin", "Produce"],
    [5, "Strawberry", "Produce"],
    [6, "Corn", "Produce"],
    [7, "Tomato", "Produce"],
    [8, "Potato", "Produce"],
    [10, "Celery", "Produce"],
    [11, "Egg", "Animal Product"],
    [12, "Cow Milk", "Animal Product"],
    [13, "Goat Milk", "Animal Product"],
    [14, "Honey", "Animal Product"],
    [15, "Cheese", "Animal Product"],
    [16, "Beef", "Meat"],
    [17, "Chicken", "Meat"],
    [18, "Pork", "Meat"],
    [19, "Turkey", "Meat"],
    [20, "Sausage", "Meat"],
    [21, "Wool", "Textiles"],
    [22, "Hemp", "Textiles"],
    [23, "Cotton", "Textiles"],
    [24, "Linen", "Textiles"],
    [25, "Silk", "Textiles"]
];

// Switch pages
function showPage(page) {
    const pages = ["markets", "vendors", "products", "orders"];

    pages.forEach(function(id) {
        document.getElementById(id).style.display = "none";
    });

    document.getElementById(page).style.display = "block";
}

// Display markets
function showMarkets() {
    const list = document.getElementById("marketList");
    list.innerHTML = "";

    markets.forEach(function(market) {
        const item = document.createElement("div");
        item.className = "item";

        const name = document.createElement("strong");
        name.textContent = market[0];

        const address = document.createElement("p");
        address.textContent = market[1];

        item.appendChild(name);
        item.appendChild(address);
        list.appendChild(item);
    });
}

// Display vendors
function showVendors() {
    const list = document.getElementById("vendorList");
    list.innerHTML = "";

    vendors.forEach(function(vendor) {
        const item = document.createElement("div");
        item.className = "item";

        const name = document.createElement("strong");
        name.textContent = vendor[0];

        const website = document.createElement("p");
        website.textContent = vendor[1];

        item.appendChild(name);
        item.appendChild(website);
        list.appendChild(item);
    });
}

// Search products
function showProducts() {
    const search = document.getElementById("searchInput")
        .value.toLowerCase();

    const list = document.getElementById("productList");
    list.innerHTML = "";

    products.forEach(function(product) {
        const name = product[1];
        const category = product[2];

        if (name.toLowerCase().includes(search) ||
            category.toLowerCase().includes(search)) {

            const item = document.createElement("div");
            item.className = "item";

            const title = document.createElement("strong");
            title.textContent = name;

            const details = document.createElement("p");
            details.textContent = "Category: " + category;

            item.appendChild(title);
            item.appendChild(details);
            list.appendChild(item);
        }
    });
}

// Add products
function loadOrderProducts() {
    const dropdown = document.getElementById("orderProduct");

    products.forEach(function(product) {
        const option = document.createElement("option");
        option.value = product[0];
        option.textContent = product[1];
        dropdown.appendChild(option);
    });
}

// Preview a pre-order
function placeOrder(event) {
    event.preventDefault();

    const customer = document.getElementById("customerID").value;
    const product = document.getElementById("orderProduct");
    const productName = product.options[product.selectedIndex].text;
    const vendor = document.getElementById("vendorID").value;
    const address = document.getElementById("address").value;

    document.getElementById("orderMessage").textContent =
        "Pre-order preview: Customer " + customer +
        " selected " + productName +
        " from Vendor " + vendor +
        " for delivery to " + address +
        ". No order has been saved to the database.";
}

showMarkets();
showVendors();
showProducts();
loadOrderProducts();
