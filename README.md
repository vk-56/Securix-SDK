# PBL2-testapp
# SDK Functionality Demo
### **Anti Screenshot Functionality**
![Anti Screenshot GIF](https://github.com/vk-56/PBL2-testapp/blob/main/Res/antiscdemo.gif)
### **Anti Screencast Functionality**
![Anti Screencast GIF](https://github.com/vk-56/PBL2-testapp/blob/main/Res/anticapturedemo.gif)
### **Root Detection Functionality**
![Root Detection GIF](https://github.com/vk-56/PBL2-testapp/blob/main/Res/rootdemo.gif)
### **Data Obfuscation Functionality**
![Data Obfuscation GIF](https://github.com/vk-56/PBL2-testapp/blob/main/Res/obfuscatedemo.gif)
# AntiScreenshot

A utility object for preventing screenshots in an Android app by setting the `FLAG_SECURE` flag on the window.

## Usage

To prevent screenshots in your app, call the `preventScreenshots` function with a valid `Context`:

```kotlin
AntiScreenshot.preventScreenshots(context)
```

## Example

To prevent screenshots in your app, call the `preventScreenshots` function with a valid `Context`:

```kotlin
// Call preventScreenshots in your Activity
class MainActivity : AppCompatActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContentView(R.layout.activity_main)
        // Prevent screenshots
        AntiScreenshot.preventScreenshots(this)
    }
}
```

## Parameters

- context: The context of the activity or application.

## Notes

- Screenshots will be prevented for the duration of the activity or application window

# DataObfuscation

A utility object for obfuscating data in various ways.

## Functions

### obfuscateData

Obfuscates the data by replacing all characters except the last 4 with an obscure character.

- **Parameters**
    - `data`: The data to obfuscate.

- **Returns**
    - A string with the data obfuscated.

### deobfuscateData

Deobfuscates the data. This function currently returns the obfuscated data as-is for demonstration purposes.

- **Parameters**
    - `obfuscatedData`: The obfuscated data to deobfuscate.

- **Returns**
    - The deobfuscated data.

### randomCharacterObfuscate

Obfuscates the data by replacing each letter or digit with a random lowercase letter.

- **Parameters**
    - `data`: The data to obfuscate.

- **Returns**
    - A string with the data obfuscated using random characters.

### customCharacterObfuscate

Obfuscates the data by replacing all letters with a custom character.

- **Parameters**
    - `data`: The data to obfuscate.
    - `charToReplace`: The custom character to use for obfuscation.

- **Returns**
    - A string with the data obfuscated using the custom character.

### shuffleDatabaseRecords

Shuffles the order of records in a list.

- **Parameters**
    - `records`: The list of records to shuffle.

- **Returns**
    - A shuffled list of records.

### maskOutData

Masks out a portion of the data with a specified mask character.

- **Parameters**
    - `data`: The data to mask out.
    - `start`: The starting index of the portion to mask out.
    - `end`: The ending index of the portion to mask out.
    - `maskChar`: The character to use for masking. Defaults to `*`.

- **Returns**
    - A string with the specified portion of the data masked out.

# RootChecker

A utility object for checking if an Android device is rooted.

## Functions

### isDeviceRooted

Checks if the device is rooted using multiple methods.

- **Returns**
    - `true` if the device is rooted, `false` otherwise.

### checkRootMethod1

Checks if the device is rooted using method 1, which looks for specific build tags associated with rooted devices.

- **Returns**
    - `true` if the device is rooted using method 1, `false` otherwise.

### checkRootMethod2

Checks if the device is rooted using method 2, which looks for common paths where root files are typically located.

- **Returns**
    - `true` if the device is rooted using method 2, `false` otherwise.

### checkRootMethod3

Checks if the device is rooted using method 3, which tries to execute the "which su" command to see if the su binary is present.

- **Returns**
    - `true` if the device is rooted using method 3, `false` otherwise.

## Usage

To check if the device is rooted, use the `isDeviceRooted` function in a coroutine:

```kotlin
viewModelScope.launch {
    val isRooted = RootChecker.isDeviceRooted()
    // Use isRooted value
}
