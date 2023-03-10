package edu.projectportal

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import edu.projectportal.database.ProjectDatabase
import edu.projectportal.database.Secret
import edu.projectportal.database.SecretDatabase
import net.sqlcipher.database.SupportFactory
import javax.crypto.KeyGenerator

const val DATABASE_NAME = "projects.db"
const val ALGORITHM_AES = "AES"
const val SHARED_PREFS_NAME = "bu.edu.projectportal.key.shared_prefs"
const val PREFS_KEY_PASSPHRASE = "test1234"

class ProjectApplication : Application() {
    lateinit var projectDatabase: ProjectDatabase

    override fun onCreate() {
        super.onCreate()
        projectDatabase = createDatabase();
    }

    private fun createDatabase(): ProjectDatabase {
        val passphrase =
            getPassphrase(applicationContext) ?: initializePassphrase(applicationContext)

        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(
            applicationContext,
            ProjectDatabase::class.java,
            DATABASE_NAME
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun initializePassphrase(context: Context): ByteArray {
        val passphrase = generatePassphrase()

        getSharedPrefs(context).edit(commit = true) {
            putString(PREFS_KEY_PASSPHRASE, passphrase.toString(Charsets.ISO_8859_1))
        }

        return passphrase
    }

    private fun getPassphrase(context: Context): ByteArray? {
        val passphraseString = getSharedPrefs(context)
            .getString(PREFS_KEY_PASSPHRASE, null)
        return passphraseString?.toByteArray(Charsets.ISO_8859_1)
    }

    private fun getSharedPrefs(context: Context): SharedPreferences {
        val masterKey =
            MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

        return EncryptedSharedPreferences.create(
            context,
            SHARED_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun generatePassphrase(): ByteArray {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES)
        keyGenerator.init(256)
        return keyGenerator.generateKey().encoded
    }

}