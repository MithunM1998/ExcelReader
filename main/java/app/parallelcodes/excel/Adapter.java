package app.parallelcodes.excel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends ListAdapter<Movie,Adapter.MyViewHolder> {


    private Context mContext;
    private List<Movie> mMovieList;
    LayoutInflater inflater;
     Movie movie=new Movie();

    protected Adapter(@NonNull DiffUtil.ItemCallback<Movie> diffCallback,List<Movie>movies) {
        super(diffCallback);
        this.mMovieList=movies;
    }

    /*protected Adapter(Context context,List<Movie>movieList){
        super(DIFF_CALLBACK);
        this.mContext=context;
        this.mMovieList=movieList;
        inflater=(LayoutInflater.from(context)) ;
    }*/
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // View view;
      //  view=inflater.inflate(R.layout.lst_template,parent,false);
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lst_template,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText( mMovieList.get(position).getMovieName());

        /*Movie movie2=getItem(position);
        holder.bind(movie2);*/
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.name);
        }

       /* public void bind(Movie m){
            title.setText(movie.getMovieName());

        }*/
    }


}
