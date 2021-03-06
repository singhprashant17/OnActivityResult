
# OnActivityResult

An Android library that solves OnActivityResult callbacks using annotation in the most simple way
- Unified implentation for both Activity and Fragments
- Generates boilerplate code for OnActivityResult callbacks and lets you focus on what matters
- Generated code is fully traceable and debuggable

<b>Usage</b>

Your Activity or Fragment will have something like this 
```java
@Override  
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {  
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 101 && resultCode == 102) {
        methodName(data);
    }
}

void methodName(Intent data) {
    final String message = data.getStringExtra("message");
    final int value = data.getIntExtra("value", 0);
}
```
change the above to 
```java
@Override  
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {  
    super.onActivityResult(requestCode, resultCode, data);
    // @here  
    ActivityResults.hook(this, requestCode, resultCode, data);  
}

// And annotate your target method like
@OnActivityResult(requestCode = 101, resultCode = 102)  
void methodName(Intent data) { 
    // this method will automatically be called if the
    // requestCode and resultCode match with that in the onActivityResult method. 
    // The method parameter holds the result intent returned.
    // The parameter "data" is that intent object passed when the activity started for result executes "setResult(RESULT_CODE, intent)")
    
    final String message = data.getStringExtra("message");
    final int value = data.getIntExtra("value", 0);
}
```

<b>Rules</b>

- The annotated method cannot be private or protected
- The annotated method must not be static
- The annotated method must have exactly one parameter of type android.content.Intent


<b>Integration</b>

Add it on your root gradle build:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add it to your app gradle file
```groovy
implementation 'com.github.singhprashant17.OnActivityResult:onactivityresult:2.0.0'  
annotationProcessor 'com.github.singhprashant17.OnActivityResult:onactivityresult-annotation-processor:2.0.0'
```

<b>Inspiration</b>
**[OnActivityResult](https://github.com/vanniktech/OnActivityResult)**

<b>License</b>

Apache-2.0
