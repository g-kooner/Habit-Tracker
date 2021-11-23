package com.example.agilesprinters;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class ForumManager extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private String UID;
    private User user;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db;

    // For search bar
    private TextInputLayout user_drop_down;
    private AutoCompleteTextView users_list;

    private ArrayList<String> arrayList_users = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter_users;
    private ArrayList<User> array_user_objects = new ArrayList<>();

    ArrayAdapter<Forum> forumAdapter;
    final private ArrayList<Forum> forumDataList = new ArrayList<>();;
    ArrayList<String> userTempList;

    private String firstName, lastName, eventDate, duration, optComment;

    private String emailId;
    private ArrayList<String> followersList = new ArrayList<>();
    private ArrayList<String> followingList = new ArrayList<>();
    private ArrayList<String> followRequestList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_manager);

        if (UID == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
            userTempList = user.getFollowingList();
            userTempList.add(UID);
        }

        System.out.println("User list is " + userTempList);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.forumn);

        // connecting to the search bar xml files
        user_drop_down = (TextInputLayout) findViewById(R.id.user_search_drop_down);
        users_list = (AutoCompleteTextView) findViewById(R.id.users_list);

        //forumDataList = new ArrayList<>();
        ListView forumList = findViewById(R.id.forumListView);
        db = FirebaseFirestore.getInstance();

        screenSetup();
        buildUsersList();

        System.out.println("Array list3 is " + forumDataList);
        forumAdapter = new forumPostList(this, forumDataList);
        forumList.setAdapter(forumAdapter);

        arrayAdapter_users = new ArrayAdapter<>(getApplicationContext(),
                R.layout.drop_down_menu, arrayList_users);
        users_list.setAdapter(arrayAdapter_users);
        users_list.setThreshold(2);

        users_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println((String) adapterView.getItemAtPosition(i));

                for (User user : array_user_objects) {
                    if (user.getEmailId().matches((String) adapterView.getItemAtPosition(i))) {
                        User userToSend = user;
                        Intent intent = new Intent(ForumManager.this, OtherUserScreen.class);
                        intent.putExtra(getString(R.string.USER_STR), userToSend);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private void buildUsersList() {
        db.collection("users").addSnapshotListener((value, error) -> {
            arrayAdapter_users.clear();
            for (QueryDocumentSnapshot doc : value) {
                if(!doc.getId().matches(user.getUserID()) && userTempList.contains(doc.getId())){
                    emailId = (String) doc.getData().get("Email ID");
                    firstName = (String)  doc.getData().get("First Name");
                    lastName = (String) doc.getData().get("Last Name");
                    followersList = (ArrayList<String>) doc.getData().get("followers");
                    followingList = (ArrayList<String>) doc.getData().get("following");
                    followRequestList = (ArrayList<String>) doc.getData().get("follow request list");

                    arrayList_users.add(emailId);
                    array_user_objects.add(
                            new User((String) doc.getData().get("UID"), firstName, lastName,
                                    emailId, followersList, followingList, followRequestList));

                }
            }
            arrayAdapter_users.notifyDataSetChanged();
        });
    }

    public void screenSetup() {

        db.collection("ForumPosts").addSnapshotListener((value, error) -> {
            forumDataList.clear();
            for (QueryDocumentSnapshot doc : value) {
                System.out.println("IDS are " + (String) doc.getData().get("UID"));
                if(userTempList.contains((String) doc.getData().get("UID"))){
                    firstName = (String) doc.getData().get("First Name");
                    lastName = (String) doc.getData().get("Last Name");
                    duration = (String) doc.getData().get("duration");
                    eventDate = (String) doc.getData().get("Event Date");
                    optComment = (String) doc.getData().get("Opt Cmt");
                    forumDataList.add(new Forum(firstName, lastName, eventDate, duration, optComment));
                    System.out.println("Array list1 is " + forumDataList);
                }
            }

            forumAdapter.notifyDataSetChanged();
        });

    }

    /**
     * This method contains the logic for switching screens by selecting an item from the navigation
     * bar.
     *
     * @param item This is the item selected by the user
     * @return Returns a boolean based on which activity the user is currently in and which item was
     * clicked.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(this, Home.class);
                intent.putExtra("user", user);
                //add bundle to send data if need
                startActivity(intent);
                finish();
                break;

            case R.id.calendar:
                Intent calendarIntent = new Intent(this, UserCalendar.class);
                calendarIntent.putExtra("user", user);
                //add bundle to send data if need
                startActivity(calendarIntent);
                finish();
                break;

            case R.id.notification:
                Intent intentNotification = new Intent(this, Notifications.class);
                intentNotification.putExtra("user", user);
                //add bundle to send data if need
                startActivity(intentNotification);
                finish();
                break;

            case R.id.forumn:
                if (this instanceof ForumManager) {
                    return true;
                } else {
                    Intent forumIntent = new Intent(this, ForumManager.class);
                    //add bundle to send data if need
                    startActivity(forumIntent);
                    finish();
                    break;
                }
        }
        return false;
    }



}