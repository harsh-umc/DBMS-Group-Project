Each of the created tables have 1 piece of data per table field, no partial key dependencies, and all non-key columns are fully dependent on their tables' primary keys with secondary tables created as need.

To break it down:
For 1NF, there are tables such as Vendor_Market that allow for many-to-many relationships without more than one piece of data in any given field.

For 2NF there are tables such as the Customer table that stores customer information so that the Customer_Order table doesn't need to store it, removing redundant information being stored in tables.

Finally, for 3NF, every table only contains information that is dependent on the tables' primary keys with all other information stored on seperate, connected tables.
