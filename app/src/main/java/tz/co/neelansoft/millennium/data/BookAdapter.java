package tz.co.neelansoft.millennium.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tz.co.neelansoft.millennium.R;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    String TAG = "BookAdapter";
    List<Book> bookList = new ArrayList<>();
    Context context;
    private OnClickListener mClickListener;
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public Book getItem(int position){
        return bookList.get(position);
    }

    public BookAdapter(Context context, OnClickListener listener) {
        this.context = context;
        this.mClickListener = listener;
    }

    public void setBookList(List<Book> books){
        this.bookList = books;
        notifyDataSetChanged();
    }
    public List<Book> getBooks(){
        return this.bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    public interface OnClickListener{
        void onClick(int itemId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageThumbnail;
        TextView tvTitle;
        TextView tvPublisher;
        TextView tvAuthor;
        TextView tvTheme;
        TextView tvYear;

        public ViewHolder(View itemView) {
            super(itemView);

            imageThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvPublisher  = itemView.findViewById(R.id.tvPublisher);
            tvTheme  = itemView.findViewById(R.id.tvTheme);
            tvYear = itemView.findViewById(R.id.tvYear);

            itemView.setOnClickListener(this);
        }

        public void bind(int position){

            Book book = getItem(position);

            Picasso.with(context).load(book.getThumbnailUrl()).into(imageThumbnail);
            Log.e(TAG,book.getThumbnailUrl());
            tvTitle.setText(book.getTitle());
            String author = book.getAuthor();
            if(author.contains(",")){
                String[] authors = author.split(",");
                author = authors[0].concat(" et al");
            }
            tvAuthor.setText(author);
            tvPublisher.setText(book.getPublisher());
            tvTheme.setText(book.getTheme());
            tvYear.setText(String.valueOf(book.getYear()));
        }

        @Override
        public void onClick(View view){
            int itemId = bookList.get(getAdapterPosition()).getId();
            mClickListener.onClick(itemId);
        }
    }
}
