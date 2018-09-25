package tz.co.neelansoft.millennium.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tz.co.neelansoft.millennium.R;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<Comment> commentList = new ArrayList<>();
    private Context context;

    public CommentsAdapter(Context context){
        this.context = context;
    }

    public void setCommentList(List<Comment> comments){
        commentList = comments;
        notifyDataSetChanged();
    }

    public List<Comment> getComments(){
        return commentList;
    }
    public long getItemId(int position){
        return 0;
    }
    public int getItemCount(){
        return commentList.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder,int position){
        holder.bind(position);
    }




    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvUser;
        TextView tvCommentBody;
        TextView tvCommentDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvCommentBody = itemView.findViewById(R.id.tvCommentBody);
            tvCommentDate = itemView.findViewById(R.id.tvCommentDate);
        }

        public void bind(int position){

            Comment comment = commentList.get(position);
            tvUser.setText(comment.getUser());
            tvCommentDate.setText(comment.getDate());
            tvCommentBody.setText(comment.getBody());
        }
    }
}
