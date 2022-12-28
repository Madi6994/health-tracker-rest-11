package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DbConfig{

    private val logger = KotlinLogging.logger {}

    //NOTE: you need the ?sslmode=require otherwise you get an error complaining about the ssl certificate
    fun getDbConnection() :Database{

        logger.info{"Starting DB Connection..."}

        val dbConfig = Database.connect(
            "jdbc:postgresql://ec2-18-215-41-121.compute-1.amazonaws.com:5432/dalspj3bonl4cr?sslmode=require",
            driver = "org.postgresql.Driver",
            user = "huuzlpdkncvubl",
            password = "e0d35b9dc56513187b557f645744cfaa89a22dc25a7a5e3aac32da437a9f18d1")

        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig
    }

}