package tz.co.neelansoft.millennium;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLTransactionRollbackException;
import java.util.ArrayList;
import java.util.List;

import tz.co.neelansoft.millennium.data.Book;
import tz.co.neelansoft.millennium.data.Config;
import tz.co.neelansoft.millennium.data.JSONParser;

public class LibraryActivity extends AppCompatActivity {

    private static final String TAG = "LibraryActivity";
    private static final String BOOK_KEY = "books";
    private static final String BOOK_ID_KEY = "id";
    private static final String BOOK_TITLE_KEY = "title";
    private static final String BOOK_AUTHOR_KEY = "author";
    private static final String BOOK_FILE_KEY = "file";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_library);
    }



    public class BooksLoader extends AsyncTaskLoader<List<Book>>{
        List<Book> bookList = new ArrayList<>();
        public BooksLoader(Context context) {
            super(context);
        }

        @Override
        public List<Book> loadInBackground(){
            JSONParser jsonParser = new JSONParser();
            try{
                URL librayUrl = new URL(Config.LIBRARY_URL);
                String jsonString = jsonParser.getJSONFromURL(librayUrl);
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
                            String book_thumbnail_url = Config.LIBRARY_URL.concat("text/").concat(book_file);
                            Bitmap book_thumbnail_image = getThumbnailFromPdfUrl(book_thumbnail_url);

                            Book book = new Book(book_id,book_title,book_author,book_file, book_thumbnail_image);
                        }
                    }
                }
                catch (JSONException je){
                    Log.e(TAG, "JSON Object error",je);
                    return null;
                }
            }
            catch (MalformedURLException e){
                Log.e(TAG, "URL error",e);
                return null;
            }
            catch (IOException ioException){
                Log.e(TAG, "JSON Parser Error",ioException);
                return null;
            }
            return null;
        }
    }
}
