package com.nganthoi.salai.tabgen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sharePreference.SharedPreference;
import template.TemplateAdapter;

public class UserLandingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreference sp;
    String role;//user role
    Context _context=this;
    List<String> list;
    ListView templateList;
    ArrayAdapter<String> arrayAdapter;
    TemplateAdapter templateAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Getting textView IDs from the drawer */
        TextView username = (TextView) findViewById(R.id.username);
        TextView usermail = (TextView) findViewById(R.id.user_email);
        TextView userrole = (TextView) findViewById(R.id.user_role);

        /**Getting template listview Id**/
        templateList = (ListView) findViewById(R.id.templatesLists);
        list = new ArrayList<String>();
        /* Getting user details from the shared preference */
        sp = new SharedPreference();
        String user_details = sp.getPreference(_context);
        try {
            JSONObject jsonObject = new JSONObject(user_details);
            username.setText(jsonObject.getString("username"));
            usermail.setText(jsonObject.getString("email"));
            role = jsonObject.getString("roles");
            userrole.setText(role);
            list = OrganisationDetails.getListOfTemplates(this,role);
        } catch (JSONException e) {
            System.out.println("Exception :" + e.toString());
        }

        /*arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        templateList.setAdapter(arrayAdapter);*/
        templateAdapter = new TemplateAdapter(UserLandingActivity.this,list);
        templateList.setAdapter(templateAdapter);
        //adding on click event for a particular item
        templateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String template_name = templateAdapter.getItem(position);
                switch(template_name){
                    case "Chat Template"://check if Chat template exist
                        Toast.makeText(_context,"You have selected chat template",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(_context,UserActivity.class));
                        break;
                    case "Reference Template":
                        Toast.makeText(_context,"You have selected reference template",Toast.LENGTH_SHORT).show();
                        break;
                    case "CME Template":
                        Toast.makeText(_context,"You have selected CME template",Toast.LENGTH_SHORT).show();
                        break;
                    case "Latest News Template":
                        Toast.makeText(_context,"You have selected News template",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "No action yet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            logout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.logout){
            logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /* function for logout */
    public void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
        alertDialogBuilder.setTitle("Logout ?");
        alertDialogBuilder.setMessage("Are you sure to logout?");
        alertDialogBuilder.setIcon(R.drawable.failure_icon);
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(_context, FirstLoginActivity.class));
                finish();
                sp.clearPreference(_context);
            }
        });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
