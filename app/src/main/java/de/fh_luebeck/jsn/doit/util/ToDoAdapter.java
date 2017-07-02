package de.fh_luebeck.jsn.doit.util;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import de.fh_luebeck.jsn.doit.R;
import de.fh_luebeck.jsn.doit.data.ToDo;
import de.fh_luebeck.jsn.doit.interfaces.ToDoListEventHandler;

/**
 * Created by USER on 14.04.2017.
 */

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private ToDoListEventHandler _caller;
    private List<ToDo> toDoList;
    private static String _tag = ToDoAdapter.class.getSimpleName();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public CardView _cardView;
        public TextView _todoNameView;
        public TextView _todoDescriptionView;
        public TextView _todoDueDateView;
        public CheckBox _todoDone;
        public CheckBox _todoFavourite;
        public ImageButton _todoEdit;

        public ViewHolder(View v) {
            super(v);
            _cardView = (CardView) v.findViewById(R.id.todo_card);
            _todoNameView = (TextView) v.findViewById(R.id.todo_name);
            _todoDescriptionView = (TextView) v.findViewById(R.id.todo_description);
            _todoDueDateView = (TextView) v.findViewById(R.id.todo_duedate);
            _todoDone = (CheckBox) v.findViewById(R.id.todo_done);
            _todoFavourite = (CheckBox) v.findViewById(R.id.todo_favourite);
            _todoEdit = (ImageButton) v.findViewById(R.id.todo_edit);
        }
    }

    public ToDoAdapter(ToDoListEventHandler caller, List<ToDo> toDoList) {
        this.toDoList = toDoList;
        this._caller = caller;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_todo_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder._todoNameView.setText(toDoList.get(position).getName());
        holder._todoDescriptionView.setText(toDoList.get(position).getDescription());
        holder._todoDueDateView.setText(DateUtils.getRelativeTimeSpanString(toDoList.get(position).getExpiry().getTime()));
        holder._todoDone.setChecked(toDoList.get(position).getDone());
        holder._todoFavourite.setChecked(toDoList.get(position).getFavourite());

        holder._todoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(_tag, "Clicked edit on " + position);
                _caller.handleEditClick(toDoList.get(position).getId());
            }
        });

        holder._todoFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _caller.handleFavoriteClick(toDoList.get(position).getId());
            }
        });

        holder._todoDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _caller.handleDoneClick(toDoList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

}
