<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#FFFFFF"
    android:padding="12dp"
    tools:context=".HomeFragment">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <!-- Header -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:paddingBottom="16dp"
            android:layout_marginTop="16dp">

            <TextView
                android:id="@+id/appTitle"
                android:layout_width="0dp"
                android:layout_weight="1"
                android:layout_height="wrap_content"
                android:text="@string/app_title"
                android:textStyle="bold"
                android:textSize="22sp"/>

        </LinearLayout>

        <!-- Expiring Soon Section -->
        <androidx.cardview.widget.CardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp"
            app:cardCornerRadius="10dp"
            app:cardElevation="4dp">

            <RelativeLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="#F2DDDC"
                android:padding="16dp">

                <TextView
                    android:id="@+id/profile"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/profile"
                    android:textColor="#000000"
                    android:textSize="34sp"
                    android:textStyle="bold" />

                <TextView
                    android:id="@+id/name"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Name: John Smith"
                    android:textColor="#000000"
                    android:textSize="24sp"
                    android:textStyle="normal"
                    android:translationY="40dp" />

                <TextView
                    android:id="@+id/name2"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Age: 21"
                    android:textColor="#000000"
                    android:textSize="24sp"
                    android:textStyle="normal"
                    android:translationY="70dp" />

                <TextView
                    android:id="@+id/name3"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Gender: Male"
                    android:textColor="#000000"
                    android:textSize="24sp"
                    android:textStyle="normal"
                    android:translationY="100dp" />

                <FrameLayout
                    android:id="@+id/imageContainer"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_alignParentEnd="true"
                    android:layout_centerVertical="true">

                    <ImageView
                        android:id="@+id/imageView"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        tools:src="@tools:sample/avatars" />

                    <ImageButton
                        android:id="@+id/imageButton"
                        android:layout_width="40dp"
                        android:layout_height="40dp"
                        android:background="#73FFFFFF"
                        android:contentDescription="@string/profile"
                        android:src="@android:drawable/ic_menu_add"
                        android:translationX="86dp"
                        android:translationY="86dp" />

                </FrameLayout>

                <LinearLayout
                    android:id="@+id/indicatorContainer"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_below="@id/expiringSoonDays"
                    android:layout_centerHorizontal="true"
                    android:layout_marginTop="16dp"
                    android:orientation="horizontal">
                    <!-- Dots will be added programmatically -->
                </LinearLayout>

            </RelativeLayout>
        </androidx.cardview.widget.CardView>

        <!-- Expired Items Section -->


        <!-- Recently Added Section -->

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            android:padding="16dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="8dp"
                android:text="Allergies"
                android:textSize="24sp"
                android:textStyle="bold" />

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="4dp"
                android:orientation="horizontal">

                <TextView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="Strawberries"
                    android:textSize="18sp" />

            </LinearLayout>

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="4dp"
                android:orientation="horizontal">

                <TextView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="Shrimp"
                    android:textSize="18sp" />

            </LinearLayout>

        </LinearLayout>

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:layout_marginBottom="8dp"
            android:text="@string/settings"
            android:textColor="#000000"
            android:textSize="20sp"
            android:textStyle="bold" />

        <androidx.cardview.widget.CardView
            android:id="@+id/recentItemsCard"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp"
            app:cardCornerRadius="10dp"
            app:cardElevation="4dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="8dp"
                android:background="#F0F0F0">

                <!-- Item 1 -->
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:padding="8dp"
                    android:background="?attr/selectableItemBackground"
                    android:clickable="true"
                    android:focusable="true">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_vertical"
                        android:layout_marginStart="16dp"
                        android:layout_weight="1"
                        android:text="Allow Location Sharing"
                        android:textColor="#000000"
                        android:textSize="16sp" />

                    <ImageButton
                        android:id="@+id/favoriteItem1"
                        android:layout_width="40dp"
                        android:layout_height="40dp"
                        android:background="?attr/selectableItemBackgroundBorderless"
                        android:contentDescription="@string/favorite"
                        android:src="@android:drawable/radiobutton_off_background" />
                </LinearLayout>

                <!-- Item 2 -->
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:padding="8dp"
                    android:background="?attr/selectableItemBackground"
                    android:clickable="true"
                    android:focusable="true">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_vertical"
                        android:layout_marginStart="16dp"
                        android:layout_weight="1"
                        android:text="Allow Access to Contacts"
                        android:textColor="#000000"
                        android:textSize="16sp" />

                    <ImageButton
                        android:id="@+id/favoriteItem2"
                        android:layout_width="40dp"
                        android:layout_height="40dp"
                        android:background="?attr/selectableItemBackgroundBorderless"
                        android:contentDescription="@string/favorite"
                        android:src="@android:drawable/radiobutton_off_background" />
                </LinearLayout>

                <!-- Item 3 -->
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:padding="8dp"
                    android:background="?attr/selectableItemBackground"
                    android:clickable="true"
                    android:focusable="true">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_vertical"
                        android:layout_marginStart="16dp"
                        android:layout_weight="1"
                        android:text="Collect User History"
                        android:textColor="#000000"
                        android:textSize="16sp" />

                    <ImageButton
                        android:id="@+id/favoriteItem3"
                        android:layout_width="40dp"
                        android:layout_height="40dp"
                        android:background="?attr/selectableItemBackgroundBorderless"
                        android:contentDescription="@string/favorite"
                        android:src="@android:drawable/radiobutton_off_background" />
                </LinearLayout>
                <!-- Item 3 -->
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:padding="8dp"
                    android:background="?attr/selectableItemBackground"
                    android:clickable="true"
                    android:focusable="true">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_vertical"
                        android:layout_marginStart="16dp"
                        android:layout_weight="1"
                        android:text="Use Data to Reccomend Recipes"
                        android:textColor="#000000"
                        android:textSize="16sp" />

                    <ImageButton
                        android:id="@+id/favoriteItem4"
                        android:layout_width="40dp"
                        android:layout_height="40dp"
                        android:background="?attr/selectableItemBackgroundBorderless"
                        android:contentDescription="@string/favorite"
                        android:src="@android:drawable/radiobutton_off_background" />
                </LinearLayout>
                <!-- Item 3 -->
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:padding="8dp"
                    android:background="?attr/selectableItemBackground"
                    android:clickable="true"
                    android:focusable="true">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_vertical"
                        android:layout_marginStart="16dp"
                        android:layout_weight="1"
                        android:text="Allow Notifications"
                        android:textColor="#000000"
                        android:textSize="16sp" />

                    <ImageButton
                        android:id="@+id/favoriteItem5"
                        android:layout_width="40dp"
                        android:layout_height="40dp"
                        android:background="?attr/selectableItemBackgroundBorderless"
                        android:contentDescription="@string/favorite"
                        android:src="@android:drawable/radiobutton_off_background" />
                </LinearLayout>
                <!-- Item 3 -->
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:padding="8dp"
                    android:background="?attr/selectableItemBackground"
                    android:clickable="true"
                    android:focusable="true">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_vertical"
                        android:layout_marginStart="16dp"
                        android:layout_weight="1"
                        android:text="Send Notifications when Items near Expire Date"
                        android:textColor="#000000"
                        android:textSize="16sp" />

                    <ImageButton
                        android:id="@+id/favoriteItem6"
                        android:layout_width="40dp"
                        android:layout_height="40dp"
                        android:background="?attr/selectableItemBackgroundBorderless"
                        android:contentDescription="@string/favorite"
                        android:src="@android:drawable/radiobutton_off_background" />
                </LinearLayout>
            </LinearLayout>
        </androidx.cardview.widget.CardView>


        <View
            android:layout_width="match_parent"
            android:layout_height="70dp" />
    </LinearLayout>
</ScrollView>
