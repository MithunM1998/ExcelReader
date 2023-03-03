package app.parallelcodes.excel;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import app.parallelcodes.excel.helpers.DBHelper;
import app.parallelcodes.excel.helpers.ExcelHelper;

public class MainActivity extends AppCompatActivity {

    static {
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
        );
    }

    TextView lbl;
   // DBHelper controller = new DBHelper(this);
    RecyclerView lv;
    Adapter adapter;
    private static final int PERMISSION_REQUEST_MEMORY_ACCESS = 0;
    private static String fileType = "";
    private View mLayout;
    private static String extensionXLS = "XLS";
    private static String extensionXLXS = "XLXS";
    ActivityResultLauncher<Intent> filePicker;
    public static final String Company = "Company";
    //Movie movie=new Movie();
    List<Movie> movielist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lbl = (TextView) findViewById(R.id.txtresulttext);
        lv = (RecyclerView) findViewById(R.id.lstView);
        mLayout = findViewById(R.id.main_layout);

        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent intent1 = result.getData();

                        Uri uri = intent1.getData();
                        ReadExcelFile(MainActivity.this
                                , uri);

                    }
                });
       // FillList();


    }

    private boolean CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            Snackbar.make(mLayout, R.string.storage_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestStoragePermission();
                }
            }).show();


            return false;
        }
    }

   /* public void FillList() {
       // String[] strings=new String[movielist.size()];
        //strings =  movielist.toArray();
        try {

            //ArrayList<HashMap<String, String>> myList = controller.getProducts();
            if (movielist.size() != 0) {
//LinearLayoutManager llm = new LinearLayoutManager(this);
                // llm.setOrientation(LinearLayoutManager.VERTICAL);
              //  lv.setLayoutManager(new LinearLayoutManager(this));
                // lv.setHasFixedSize(true);

            }
        } catch (Exception ex) {
            Toast("FillList error: " + ex.getMessage(), ex);
        }
    }*/

    public void ReadExcelFile(Context context, Uri uri) {
        try {
            InputStream inStream;
            Workbook wb = null;

            try {
                inStream = context.getContentResolver().openInputStream(uri);

                if (fileType == extensionXLS)
                    wb = new HSSFWorkbook(inStream);
                else
                    wb = new XSSFWorkbook(inStream);

                inStream.close();
            } catch (IOException e) {
                lbl.setText("First " + e.getMessage().toString());
                e.printStackTrace();
            }

          //  DBHelper dbAdapter = new DBHelper(this);
            Sheet sheet1 = wb.getSheetAt(0);

           /* dbAdapter.open();
            dbAdapter.delete();
            dbAdapter.close();
            dbAdapter.open();
            ExcelHelper.insertExcelToSqlite(dbAdapter, sheet1);*/


            //String[] str=new String[10];
            //int count=0;
            for (Iterator<Row> rit = sheet1.rowIterator(); rit.hasNext(); ) {
                Row row= rit.next();


                //movie.setMovieName().getStringCellValue().toString());
               // row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
               // row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
              //Log.d("list item", row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());

          String name=row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();

        //  str[count]=name;

              //  movie.setMovieName(str[count]);
            //    count++;
                Movie movie=new Movie(name);
                movielist.add(movie);
               // movielist.add(  row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            }





        //Log.d("listsize", String.valueOf(movielist));
           // System.out.println(movielist);
            //dbAdapter.close();

            lv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
            adapter=new Adapter(Movie.itemCallback,movielist);

            lv.setLayoutManager(new LinearLayoutManager(this));

            lv.setAdapter(adapter );
            adapter.submitList(movielist);
           // FillList();
        } catch (Exception ex) {
            Toast("ReadExcelFile Error:" + ex.getMessage().toString(), ex);
        }



    }


    public void ChooseFile() {
        try {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.addCategory(Intent.CATEGORY_OPENABLE);

            if (fileType == extensionXLS)
                fileIntent.setType("application/vnd.ms-excel");
            else
                fileIntent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            filePicker.launch(fileIntent);
        } catch (Exception ex) {
            Toast("ChooseFile error: " + ex.getMessage().toString(), ex);

        }
    }

    void Toast(String message, Exception ex) {
        if (ex != null)
            Log.e("Error", ex.getMessage().toString());
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_MEMORY_ACCESS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                OpenFilePicker();
            } else {
                Snackbar.make(mLayout, R.string.storage_access_denied,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void requestStoragePermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_MEMORY_ACCESS);

        } else {
            Snackbar.make(mLayout, R.string.storage_unavailable, Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_MEMORY_ACCESS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_import_xls) {
            fileType = extensionXLS;
            OpenFilePicker();
        } else if (id == R.id.action_import_xlxs) {
            fileType = extensionXLXS;
            OpenFilePicker();
        }

        return super.onOptionsItemSelected(item);
    }

    public void OpenFilePicker() {
        try {
            if (CheckPermission()) {
                ChooseFile();
            }
        } catch (ActivityNotFoundException e) {
            lbl.setText("No activity can handle picking a file. Showing alternatives.");
        }

    }
}