package tz.co.neelansoft.millennium;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tz.co.neelansoft.millennium.data.Book;
import tz.co.neelansoft.millennium.data.BookAdapter;
import tz.co.neelansoft.millennium.data.Config;
import tz.co.neelansoft.millennium.data.JSONParser;

public class LibraryActivity extends AppCompatActivity implements BookAdapter.OnClickListener{

    private static final String TAG = "LibraryActivity";
    private static final String BOOK_KEY = "books";
    private static final String BOOK_ID_KEY = "id";
    private static final String BOOK_TITLE_KEY = "title";
    private static final String BOOK_AUTHOR_KEY = "author";
    private static final String BOOK_FILE_KEY = "file";
    private static final String BOOK_PUBLISHER_KEY = "publisher";
    private static final String BOOK_THEME_KEY = "theme";
    private static final String BOOK_EDITION_KEY = "edition";
    private static final String BOOK_PAGES_KEY = "pages";
    private static final String BOOK_YEAR_KEY = "year";
    private static final int LOADER_ID = 100;

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_library);

        bookAdapter = new BookAdapter(this,this);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar  = findViewById(R.id.progressBar);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(bookAdapter);
        new BooksLoader().execute();

    }

    @Override
    public void onClick(int itemId){
        Toast.makeText(this,"Clicked Id: "+itemId,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.library_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int menuItemId = menuItem.getItemId();
        if(menuItemId == R.id.app_bar_search){
        }
        return super.onOptionsItemSelected(menuItem);
    }
    public class BooksLoader extends AsyncTask<String,Void,String> {
        List<Book> bookList = new ArrayList<>();
        @Override
        protected String doInBackground(String... args){
            JSONParser jsonParser = new JSONParser();
            try{
                URL librayUrl = new URL(Config.BOOKS_URL);
                String jsonString = jsonParser.getJSONFromURL(librayUrl);
                Log.e(TAG,jsonString);
                try{
                    JSONObject jo = new JSONObject(jsonString);
                    JSONArray books = jo.getJSONArray(BOOK_KEY);
                    if(books != null){
                        for(int i=0; i < books.length();i++) {
                            JSONObject jsonBook = books.getJSONObject(i);
                            int book_id = Integer.parseInt(jsonBook.getString(BOOK_ID_KEY));
                            String book_title = jsonBook.getString(BOOK_TITLE_KEY);
                            String book_author = jsonBook.getString(BOOK_AUTHOR_KEY);
                            String book_file = jsonBook.getString(BOOK_FILE_KEY);
                            String book_publisher = jsonBook.getString(BOOK_PUBLISHER_KEY);
                            String book_theme = jsonBook.getString(BOOK_THEME_KEY);
                            int book_edition = Integer.parseInt(jsonBook.getString(BOOK_EDITION_KEY));
                            int book_year = Integer.parseInt(jsonBook.getString(BOOK_YEAR_KEY));
                            int book_pages = Integer.parseInt(jsonBook.getString(BOOK_PAGES_KEY));
                            String book_thumbnail_url = Config.THUMBNAILS_URL.concat(book_file.substring(0,book_file.length()-3).concat("png"));

                            Book book = new Book(book_id,book_title,book_author,book_file, book_publisher,book_theme,book_edition,book_year,book_pages,book_thumbnail_url);
                            bookList.add(book);
                        }

                    }

                }
                catch (JSONException je){
                    Log.e(TAG, "JSON Object error",je);

                }
            }
            catch (MalformedURLException e){
                Log.e(TAG, "URL error",e);

            }
            catch (IOException ioException){
                Log.e(TAG, "JSON Parser Error",ioException);

            }
            return null;
        }
        @Override
        protected void onPostExecute(String s){
            progressBar.setVisibility(View.GONE);
            bookAdapter.setBookList(bookList);
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);

        }
    }
}
