package unibo.esiot2024;

import java.sql.SQLException;

import unibo.esiot2024.central.db_access.impl.DatabaseAccessHandlerImpl;

/**
 * Launcher class for the backend application.
 */
public final class Application {

    private Application() { }

    /**
     * Main method for the backend application.
     * @param args eventual command line arguments passed to the application.
     */
    public static void main(final String[] args) {
        new GUI();
    }


    /**
     * Launches the backend application.
     * @param dbUser the username for the database connection.
     * @param dbPass the password for the database connetcion.
     * @throws SQLException
    */
    public static void launch(final String dbUser, final String dbPass) {
        //boolean excepted = false;
        try {
            new DatabaseAccessHandlerImpl(dbUser, dbPass);
        } catch (final SQLException e) {
            //excepted = true;
            new GUI();
        }

        /**
         * TODO
         * qui inizializza le altre componenti del controller assicurandoti che l'inizializzazione del database vada
         * a buon fine. Sopra solo il controller centrale con l'accesso diretto al DB.
         */
        /*if (!excepted) { }*/
    }
}
