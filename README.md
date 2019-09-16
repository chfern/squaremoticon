![Squaremoticon Happy Logo](demo/cheerfulemot.png)
![Squaremoticon Sad Logo](demo/sademot.png)
# squaremoticon
Android emoticon with facial expression transitions  

## How to Use
This library is available through jcenter and can be included as follow:
```
implementation "com.fernandochristyanto:squaremoticon:$version"
```
Current version: 1.0.0     

## Emoticon
* CheerfulSquareEmoticon
  ```
  <com.fernandochristyanto.squaremoticon.CheerfulSquareEmoticon
        android:id="@+id/smiley"
        android:layout_width="120dp"
        android:layout_height="120dp"/>
  ```
  You can set the emoticon initial state by code
  ``` smiley.setEmoticonState(emoticonState: Int)```   
  States:
  * CheerfulSquareEmoticon.EMOT_STATE_HAPPY
  * CheerfulSquareEmoticon.EMOT_STATE_SAD   
  
  Transitions
  * ```emot.animateSad(durationMillis: Long)```   
    ![Happy to Sad](demo/happytosad.gif)
  * ```emot.animateHappy(durationMillis: Long)```   
    ![Sad to Happy](demo/sadtohappy.gif)
