<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingTop="10dp"
    tools:context=".HomeFragment">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_weight="1">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical" >

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:shadowColor="#00C31F1F"
                    android:text="Welcome to Kitchen Kompanion!"
                    android:textAlignment="center"
                    android:textColor="#6E02B1"
                    android:textSize="34sp"
                    android:textStyle="bold" />

                <TextView
                    android:id="@+id/textView2"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="TextView"
                    android:textAlignment="center"
                    android:textColor="#6E02B1"
                    android:textSize="20sp"
                    android:textStyle="bold"
                    tools:text="Shopping " />

                <ImageButton
                    android:id="@+id/imageButton"
                    android:layout_width="match_parent"
                    android:layout_height="110dp"
                    android:layout_margin="0dp"
                    android:layout_marginStart="10dp"
                    android:layout_marginLeft="10dp"
                    android:layout_marginTop="10dp"
                    android:layout_marginBottom="20dp"
                    android:layout_weight="100"
                    android:adjustViewBounds="false"
                    android:contentDescription="recipes button"
                    android:cropToPadding="true"
                    android:maxWidth="100dp"
                    android:maxHeight="100dp"
                    android:scaleType="centerInside"
                    tools:src="@drawable/shopping" />

                <TextView
                    android:id="@+id/textView4"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="15dp"
                    android:text="TextView"
                    android:textAlignment="center"
                    android:textColor="#6E02B1"
                    android:textSize="20sp"
                    android:textStyle="bold"
                    tools:text="Inventory " />

                <ImageButton
                    android:id="@+id/imageButton3"
                    android:layout_width="match_parent"
                    android:layout_height="110dp"
                    android:layout_margin="0dp"
                    android:layout_marginStart="10dp"
                    android:layout_marginLeft="10dp"
                    android:layout_marginTop="10dp"
                    android:layout_marginBottom="20dp"
                    android:layout_weight="100"
                    android:adjustViewBounds="false"
                    android:contentDescription="recipes button"
                    android:cropToPadding="true"
                    android:maxWidth="100dp"
                    android:maxHeight="100dp"
                    android:scaleType="centerInside"
                    android:src="@drawable/inventory" />

                <TextView
                    android:id="@+id/textView6"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="15dp"
                    android:layout_weight="1"
                    android:text="TextView"
                    android:textAlignment="center"
                    android:textColor="#6E02B1"
                    android:textSize="20sp"
                    android:textStyle="bold"
                    tools:text="Recipes" />

                <ImageButton
                    android:id="@+id/imageButton4"
                    android:layout_width="match_parent"
                    android:layout_height="110dp"
                    android:layout_margin="0dp"
                    android:layout_marginStart="10dp"
                    android:layout_marginLeft="10dp"
                    android:layout_marginTop="10dp"
                    android:layout_marginBottom="20dp"
                    android:layout_weight="100"
                    android:adjustViewBounds="false"
                    android:contentDescription="recipes button"
                    android:cropToPadding="true"
                    android:maxWidth="100dp"
                    android:maxHeight="100dp"
                    android:scaleType="fitCenter"
                    android:src="@drawable/recipies" />

                <TextView
                    android:id="@+id/textView5"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="15dp"
                    android:text="TextView"
                    android:textAlignment="center"
                    android:textColor="#6E02B1"
                    android:textSize="20sp"
                    android:textStyle="bold"
                    tools:text="Profile and Settings" />

                <ImageButton
                    android:id="@+id/imageButton5"
                    android:layout_width="match_parent"
                    android:layout_height="110dp"
                    android:layout_margin="0dp"
                    android:layout_marginStart="10dp"
                    android:layout_marginLeft="10dp"
                    android:layout_marginTop="10dp"
                    android:layout_marginBottom="20dp"
                    android:layout_weight="1"
                    android:adjustViewBounds="false"
                    android:contentDescription="recipes button"
                    android:cropToPadding="true"
                    android:maxWidth="100dp"
                    android:maxHeight="100dp"
                    android:scaleType="fitCenter"
                    android:src="@drawable/recipies"
                    tools:src="@android:drawable/ic_menu_manage" />
            </LinearLayout>
        </ScrollView>

    </LinearLayout>

</RelativeLayout>
