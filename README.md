# projectportal

This sample project is used to Demonstrate On-Device SQLLite Encryption using SQLCipher and Room Database

This document covers

1. Using Cryptography dependencies to generate and store encyrption keys in EncryptedSharedPreferences
2. Encrypting SQLite Database with SQL Cipher
3. Verifying ondevice database is encrypted

---

## Using Cryptography dependencies to generate and store encryption keys in EncryptedSharedPreferences

---

### 1. Add Dependencies

- In App Level `build.gradle`, add below dependencies

```Kotlin
dependencies{
	// Dependencies creating ViewModels and LiveData
	implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.5.1'
	implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1'

	// Dependencies for Room Database
	def roomVersion = "2.5.0"
	implementation("androidx.room:room-runtime:$roomVersion")
	implementation("androidx.room:room-ktx:$roomVersion")
	annotationProcessor("androidx.room:room-compiler:$roomVersion")
	kapt("androidx.room:room-compiler:$roomVersion")

	// Dependency for SQL Cipher
	implementation 'net.zetetic:android-database-sqlcipher:4.5.3'

	// Dependencies for Key Generation and EncryptedSharedPreferences
	implementation "androidx.security:security-crypto:1.0.0"
	implementation "androidx.security:security-crypto-ktx:1.1.0-alpha05"

}
```

### 2. Generate Encryption Key

- We are using **AES-256 Encryption** for generating the key

```Kotlin
const val ALGORITHM_AES = "AES"

private fun generatePassphrase(): ByteArray {
    val keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES)
    keyGenerator.init(256)
    return keyGenerator.generateKey().encoded
}
```

### 3. Create EncryptedSharedPreferences

#### Create a unique shared preferences name

```Kotlin
const val SHARED_PREFS_NAME = "bu.edu.projectportal.key.shared_prefs"
```

#### Create a Master key to encrypt shared preferences with

```Kotlin
val masterKey =
    MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
```

#### Create EncryptedSharedPreference using the newly generated `masterKey`

```Kotlin
EncryptedSharedPreferences.create(
    context,
    SHARED_PREFS_NAME,
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

- We use the unique `SHARED_PREFS_NAME` and the `masterKey` to create the EncryptedSharedPreference

#### Here's the entire code for creating EncryptedSharedPreference

```Kotlin
const val SHARED_PREFS_NAME = "bu.edu.projectportal.key.shared_prefs"

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

```

### 4. Store Encryption Key in SharedPreference

```Kotlin
private fun initializePassphrase(context: Context): ByteArray {
    val passphrase = generatePassphrase()  // returns the AES-256 bit key created earlier (check step 2)

	// getSharedPrefs is defined in step 3
    getSharedPrefs(context).edit(commit = true) {
        putString(PREFS_KEY_PASSPHRASE, passphrase.toString(Charsets.ISO_8859_1))
    }

    return passphrase
}
```

## Encrypting SQLite Database with SQL Cipher

---

- We use the passphrase key from the EncryptedSharedPreference to encrypt our SQLite Database

### 1. Read the key from EncryptedSharedPreference

```Kotlin
private fun getPassphrase(context: Context): ByteArray? {
    val passphraseString = getSharedPrefs(context)
        .getString(PREFS_KEY_PASSPHRASE, null)
    return passphraseString?.toByteArray(Charsets.ISO_8859_1)
}
```

### 2. Create Database Entity

```Kotlin
@Entity(tableName = "projects_list")
data class Project(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "desc")
    var description: String,

    @ColumnInfo(name = "authors")
    var authors: String,

    @ColumnInfo(name = "projectLinks")
    var projectLinks: String,

    @ColumnInfo(name = "isFav")
    var isFav: Boolean = false,

    @ColumnInfo(name = "keywords")
    var keywords: String,

    @ColumnInfo(name = "programmingLanguagesUsed")
    var programmingLanguagesUsed: String
)
```

### 3. Create Room Database with all the entities

```Kotlin
@Database(entities = [Project::class], version = 1)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}
```

### 4. Use the Key to Encrypt Room Database

```Kotlin
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
```

In the above code,

- We first try to check if the key already exists in SharedPreferences, if it doesn't exist, we create a new Key and store it
  `val passphrase =  getPassphrase(applicationContext) ?: initializePassphrase(applicationContext)`
- Create a Support factory instance from SQL Cipher using the passphrase
  `val factory = SupportFactory(passphrase)`
- Create Room Database, using Room Database Builder and the SupportFactory instance

```
Room.databaseBuilder(
        applicationContext,
        ProjectDatabase::class.java,
        DATABASE_NAME
    )
        .openHelperFactory(factory)
        .fallbackToDestructiveMigration()
        .build()
}
```

- We have created an Encrypted SQLite Database and can implement methods to perform basic CRUD Operations as per Room Database Documentation.
- There are no additional changes that are needed to be done for implementing CRUD Operations.

## Verifying ondevice database is encrypted

---

- To verify if the database is encrypted or not download the sqlite database to your locally.
- In Android Studio, Go to **Device File Explorer** -> **Data** -> **Data** -> **<APP_PACKAGE_NAME>** -> **databases**
- Right Click on the db file and click Save As to download it to your local machine.

![[Pasted image 20230311015002.png | 500]]

- Once the database file is downloaded to your local machine, navigate to the downloaded folder through terminal and run below command

```Bash
hexdump -C <sqlite_database>.db
```

Output:
![[Pasted image 20230311015219.png | 800]]

- One of the cons to using SQLCipher for Encrypting Room Database is that we can no longer access the contents of the database from the Database Inspector in Android Studio.

![[Pasted image 20230311014433.png]]
