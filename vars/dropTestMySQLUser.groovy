#!/usr/bin/env groovy

/*
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbUser:    The name of the database user to drop the test user with.
        dbPass:    The password of the database user.
        testUser:  The username of the test user to be dropped. Default: "testdb_user_${env.BUILD_NUMBER}"
        
*/
def call(String dbUserName, String dbPassword, String dbSchemaName, String dbDropUserName = '', String mysqlPath = '', String mysqlPort = '') {

    configuration = [:]
    configuration.dbUser = dbUserName
    configuration.dbPass = dbPassword
    configuration.dbName = dbSchemaName
    configuration.mysqlPath = mysqlPath
    configuration.mysqlPort = mysqlPort
    def dropconfig = evaluateMySQLConfiguration(configuration)
    echo "====================================== Debug ======================================================="
    echo "Username to drop: ${dbDropUserName}"
    echo "====================================== /Debug ======================================================="
    if (dbDropUserName == null || dbDropUserName == '') {
        // Define the test user parameters here
        String test_username = "testdb_user_${env.MYSQL_UUID}"
        def test_user = test_username.take(32)
    }
    // Run shell commands to drop the user here
    def DROP_SQL = "DROP USER '${test_user}'@'%';"
    def REVOKE_SQL = "REVOKE ALL PRIVILEGES, GRANT OPTION FROM '${test_user}'@'%';" +
        "REVOKE ALL PRIVILEGES, GRANT OPTION FROM '${test_user}'@'localhost';"
    def SHELL_CMD = "\"${dropconfig.mysqlPath}\" -u \"${dropconfig.dbUser}\" --password=\"${dropconfig.dbPass}\" <<-EOF\n${DROP_SQL}${REVOKE_SQL}\nEOF"
    sh "${SHELL_CMD}"
}
