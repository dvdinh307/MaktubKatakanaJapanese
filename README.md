# MaktubKatakanaJapanese
Only import katakana chacracter japanese in edittext.
### How to using with Android studio.
### Step 1 : Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
 ### Step 2. Add the dependency
 	dependencies {
	        compile 'com.github.dvdinh307:MaktubKatakanaJapanese:1.0'
	}

#### Values Suggestion with keyboad japanses
![2](https://cloud.githubusercontent.com/assets/4903373/25376987/58baaab4-29d0-11e7-9f14-2a6911a8a489.png)

When you chocie suggesstion or done. Will check if character is katakana or not. If not . Remove this character.

![1](https://cloud.githubusercontent.com/assets/4903373/25376986/5867f242-29d0-11e7-8449-4504436d32f6.png)

Hope this help someone.
Please feed back for me if you have error with my custom edittext japanese katakana character.
Thanks.
