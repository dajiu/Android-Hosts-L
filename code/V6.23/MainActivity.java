package com.lack006.hosts_l;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lack006.hosts_l.enablegooglelocation.EnableShell;

import java.io.InputStream;

import static com.lack006.hosts_l.Del.Delhosts;
import static com.lack006.hosts_l.Del.Deloldfile;
import static com.lack006.hosts_l.Installbusybox.Install;
import static com.lack006.hosts_l.Reboot.Reboot_switch;
import static com.lack006.hosts_l.Showchoose.ADchoose;
import static com.lack006.hosts_l.Showchoose.ARchoose;
import static com.lack006.hosts_l.Showchoose.REchoose;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView AD, RE, AR;
    Button btn_AD, btn_RE, btn_AR;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        context = MainActivity.this;


        final String URL_AD = context.getString(R.string.URL_AD);

        AD = (TextView) findViewById(R.id.AD);
        RE = (TextView) findViewById(R.id.RE);
        AR = (TextView) findViewById(R.id.AR);
        btn_AD = (Button) findViewById(R.id.btn_AD);
        btn_RE = (Button) findViewById(R.id.btn_RE);
        btn_AR = (Button) findViewById(R.id.btn_AR);

        Checkroot();
        Deloldfile(context);

        CheckNet(AD, RE, AR, btn_AD, btn_RE, btn_AR);
//        final SharedPreferences mPreferences = getSharedPreferences(context.getString(R.string.Config_file), ContextWrapper.MODE_PRIVATE);
        SharedPreferences mPreferences = getSharedPreferences(context.getString(R.string.Config_file), 0);
        int Z = mPreferences.getInt(context.getString(R.string.Config_file_AutoStart),0);
        if (Z==1) {
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, R.string.Check_permission, Toast.LENGTH_LONG).show();
                Location_OFF();
            }
        }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                final SharedPreferences mPreferences = getSharedPreferences(context.getString(R.string.Config_file), ContextWrapper.MODE_PRIVATE);
//        boolean bStart = mPreferences.getBoolean(context.getString(R.string.Config_file_AutoStart), false);
//        if (bStart) {
//            Toast.makeText(MainActivity.this, R.string.Boot_start_enable, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(MainActivity.this, R.string.Boot_start_disable, Toast.LENGTH_SHORT).show();
//        }
//        Button test = (Button) findViewById(R.id.startbtn);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = mPreferences.edit();
//                editor.putBoolean(context.getString(R.string.Config_file_AutoStart), true);
//                editor.apply();
//                Toast.makeText(MainActivity.this, R.string.Boot_start_enable, Toast.LENGTH_SHORT).show();
//            }
//        });

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        btn_AD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog mpDialog = new ProgressDialog(MainActivity.this);
                Context context = MainActivity.this;
                Delhosts(context);
                ADchoose(mpDialog, context, URL_AD, btn_AD, btn_RE, btn_AR);
            }


        });
        btn_RE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog mpDialog = new ProgressDialog(MainActivity.this);
                Context context = MainActivity.this;
                Delhosts(context);
//                Downloadhosts.Download(mpDialog, context, URL_RE, btn_AD, btn_RE, btn_AR,0,0,0,0);
                REchoose(mpDialog, context, null, btn_AD, btn_RE, btn_AR);
            }
        });
        btn_AR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog mpDialog = new ProgressDialog(MainActivity.this);
                Context context = MainActivity.this;
                Delhosts(context);
//                Downloadhosts.Download(mpDialog, context, URL_AR, btn_AD, btn_RE, btn_AR,0,0,0,0);
                ARchoose(mpDialog, context, null, btn_AD, btn_RE, btn_AR);
            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Deloldfile(context);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Toast.makeText(MainActivity.this, R.string.Help_words, Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_refresh) {
            Deloldfile(context);
            Checkversion(AD, RE, AR, btn_AD, btn_RE, btn_AR);
            Toast.makeText(MainActivity.this, R.string.Refresh_words, Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Refresh_DNS) {
            RefreshDNS.RefreshDNS();
            Toast.makeText(MainActivity.this, R.string.Refresh_DNS_complete, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_Backup) {

            Backup();

        } else if (id == R.id.nav_Rotate) {
            Delhosts(context);
            Unzipdefault();
            Toast.makeText(MainActivity.this, R.string.Rotate_complete, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_Reboot) {
            Reboot();
        } else if (id == R.id.nav_Busybox) {
            Context context = MainActivity.this;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            Install(builder, context);
        } else if (id == R.id.nav_Update) {
            Context context = MainActivity.this;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.UpdateURL)));
            startActivity(browserIntent);
        } else if (id == R.id.nav_Log) {
            new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(false)
                    .setTitle(R.string.Android_hosts_l)
                    .setMessage(R.string.About_content)
                    .setPositiveButton(R.string.AD_Continue,
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                }
                            }
                    ).show();

        } else if (id == R.id.nav_Remove) {
            Removemark.removemark(context);
        } else if (id == R.id.nav_Location) {
            new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(false)
                    .setTitle(R.string.Enable_Location)
                    .setMessage(R.string.Location_Message)
                    .setPositiveButton(R.string.Location_Disable,
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    Location_OFF();

                                }
                            }
                    )
                    .setNeutralButton(R.string.Location_Enable, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                                    Manifest.permission.READ_PHONE_STATE);
                            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {

                                new AlertDialog.Builder(MainActivity.this)
                                        .setCancelable(false)
                                        .setTitle(R.string.Permission_title)
                                        .setMessage(R.string.Permission_Message)
                                        .setPositiveButton(R.string.Cancel,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialoginterface, int i) {
                                                        Location_OFF();
                                                    }
                                                }
                                        )
                                        .setNeutralButton(R.string.Ask_Permission, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                                Ask_Permission();
                                            }
                                        })
                                        .setNegativeButton(R.string.Config, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);

                                            }
                                        }).show();


                            } else {
                                Location_ON();
                            }

                        }
                    }).show();


        } else if (id == R.id.nav_Exit) {
            Deloldfile(context);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void Checkroot() {

        String command[] = {"su", "-c", "ls", "/data"};
        Shell shell = new Shell();
        String text = shell.sendShellCommand(command);
        if ((text.contains("app")) || (text.contains("anr")) || (text.contains("user")) || (text.contains("data"))) {
            String apkRoot = "chmod 777 " + getPackageCodePath(); // SD卡分区路径，也可能是mmcblk1随系统版本定，当前程序路径请用getPackageCodePath();
            Checkroot.RootCmd(apkRoot);
        } else {
            new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle(R.string.Tip).setMessage(R.string.No_root).setPositiveButton(R.string.Exit, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    finish();
                }
            }).show();
        }
    }


    public void CheckNet(TextView AD, TextView RE, TextView AR, Button btn_AD, Button btn_RE, Button btn_AR) {
        if (!isNetworkConnected()) {
            new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle(R.string.Tip).setMessage(R.string.No_network).setPositiveButton(R.string.Exit, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    finish();
                }
            }).show();
        } else {

            Checkversion(AD, RE, AR, btn_AD, btn_RE, btn_AR);

        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public void Backup() {
        Context context = MainActivity.this;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        Backup.Backuphosts(context, builder, btn_AD, btn_RE, btn_AR);
    }


    public void UnzipBusybox() {

        Context context = MainActivity.this;
        InputStream is = getResources().openRawResource(
                R.raw.lack006);
        Unzipbusybox.Unzipbusybox(context, is);

    }

    public void Unzipdefault() {

        Context context = MainActivity.this;
        InputStream is = getResources().openRawResource(
                R.raw.jttqbcsmwxhxy);
        Unzipdefault.UnzipDefault(context, is, btn_AD, btn_RE, btn_AR);

    }

    public void Checkversion(TextView AD, TextView RE, TextView AR, Button btn_AD, Button btn_RE, Button btn_AR) {
        ProgressDialog mpDialog = new ProgressDialog(MainActivity.this);
        Context context = MainActivity.this;
        Checkversion.Checkversion(mpDialog, context, AD, RE, AR, btn_AD, btn_RE, btn_AR);
        UnzipBusybox();


    }

    public void Reboot() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        Reboot_switch(builder);
    }


    public void Location_ON() {
        try {
            final SharedPreferences mPreferences = getSharedPreferences(context.getString(R.string.Config_file), ContextWrapper.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(context.getString(R.string.Config_file_AutoStart), 1);
            editor.apply();
            Toast.makeText(MainActivity.this, R.string.Boot_start_enable, Toast.LENGTH_SHORT).show();
            EnableShell.enableshell(context);

        } catch (Exception ignored) {
        }
    }

    public void Location_OFF() {
        final SharedPreferences mPreferences = getSharedPreferences(context.getString(R.string.Config_file), ContextWrapper.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(context.getString(R.string.Config_file_AutoStart), 0);
        editor.apply();
        Toast.makeText(MainActivity.this, R.string.Boot_start_disable, Toast.LENGTH_SHORT).show();
    }

    public void Ask_Permission() {
        final int REQUEST_CODE_ASK_PERMISSIONS = 6;
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                REQUEST_CODE_ASK_PERMISSIONS);
    }


}
