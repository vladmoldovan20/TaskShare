package vlad.taskshare;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private DatabaseReference databaseReference;

    private EditText editTextName,editTextTitle;
    private Button buttonSave;

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(this, LoginAvtivity.class));
        }

        databaseReference= FirebaseDatabase.getInstance().getReference();

        editTextName=(EditText) findViewById(R.id.editTextName);
        editTextTitle=(EditText) findViewById(R.id.editTextTitle);
        buttonSave=(Button) findViewById(R.id.buttonSave);

        FirebaseUser user= firebaseAuth.getCurrentUser();



        textViewUserEmail=(TextView) findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome " + user.getEmail()+"!");
        buttonLogout=(Button) findViewById(R.id.buttonLogout);

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void saveUserInformation(){
        String name= editTextName.getText().toString().trim();
        String title=editTextTitle.getText().toString().trim();

        UserInformation userInformation=new UserInformation(name, title);

        FirebaseUser user=firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue((userInformation));

        Toast.makeText(this,"Information saved!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginAvtivity.class));
        }
        if(view==buttonSave){
            saveUserInformation();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
