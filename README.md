# Color-O-Matic
[![](https://jitpack.io/v/GrenderG/Color-O-Matic.svg)](https://jitpack.io/#GrenderG/Color-O-Matic)

Beautiful Color Picker dialog for Android 14+ based on [Chroma by Priyesh Patel](https://github.com/ItsPriyesh/chroma) and [VintageChroma by Pavel Sikun](https://github.com/MrBIMC/VintageChroma).

Screenshots
--

<img src="https://raw.githubusercontent.com/GrenderG/Color-O-Matic/master/art/scr1.png" width="250">
<img src="https://raw.githubusercontent.com/GrenderG/Color-O-Matic/master/art/scr2.png" width="250">
<img src="https://raw.githubusercontent.com/GrenderG/Color-O-Matic/master/art/scr3.png" width="250">
<img src="https://raw.githubusercontent.com/GrenderG/Color-O-Matic/master/art/scr4.png" width="758">
<img src="https://raw.githubusercontent.com/GrenderG/Color-O-Matic/master/art/scr5.png" width="758">

Repository
--

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Dependency
--

Add this to your module's `build.gradle` file (make sure the version matches the JitPack badge above):

```gradle
dependencies {
	...
	compile 'com.github.GrenderG:Color-O-Matic:1.1.1'
}
```

Usage
--

To display a color picker `DialogFragment`:

``` java
  new ColorOMaticDialog.Builder()
      .initialColor(Color.WHITE)
      .colorMode(ColorMode.ARGB) // RGB, ARGB, HVS
      .indicatorMode(IndicatorMode.HEX) // HEX or DECIMAL; Note that using HSV with IndicatorMode.HEX is not recommended
      .onColorSelected(new OnColorSelectedListener() {
          @Override
          public void onColorSelected(@ColorInt int i) {
              // do your stuff
          }
      })
      .showColorIndicator(true) // Default false, choose to show text indicator showing the current color in HEX or DEC (see images) or not
      .create()
      .show(getFragmentManager(), "ColorOMaticDialog");
```

See [sample project](https://github.com/GrenderG/Color-O-Matic/tree/master/sample) for more info.
