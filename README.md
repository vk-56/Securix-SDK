# PBL2-testapp
# AntiScreenshot

A utility object for preventing screenshots in an Android app by setting the `FLAG_SECURE` flag on the window.

## Usage

To prevent screenshots in your app, call the `preventScreenshots` function with a valid `Context`:

```
AntiScreenshot.preventScreenshots(context)
```

## Example

To prevent screenshots in your app, call the `preventScreenshots` function with a valid `Context`:

```
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