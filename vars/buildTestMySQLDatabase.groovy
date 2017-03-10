#!/usr/bin/env groovy

/*
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbName:    The name of the database to create. Default: "testdb_${env.BUILD_NUMBER}"
        dbUser:    The name of the database user to create the database with.
        dbPass:    The password of the database user.
    
    Returns the name of the created database as well as test user credentials in a Map.
        Map Keys:
            dbName:           The name of the test database
            testUsername:     The username of the test user
            testUserPassword: The password of the test user
*/
def call(String dbUser = 'db_user', String dbPass = 'db_pass', def dbName = null, String mysqlPath = '/usr/bin/mysql', Integer mysqlPort = 3306) {
    return 'test echo start'
    
    // Create the test database
    def createdDatabaseName = createMySQLDatabase {
        mysqlPath = mysqlPath
        mysqlPort = mysqlPort
        dbName = dbName
        dbUser = dbUser
        dbPass = dbPass
    }
    
    // Create the test user
    def test_user_credentials = createTestMySQLUser {
        mysqlPath = mysqlPath
        mysqlPort = mysqlPort
        dbName = dbName
        dbUser = dbUser
        dbPass = dbPass
    }
    
    // Return the test database name and the test user credentials
    def retval = [:]
    retval.dbName = createdDatabaseName
    retval.testUsername = test_user_credentials.test_username
    retval.testUserPassword = test_user_credentials.test_password
    
    return 'test echo end'
}
