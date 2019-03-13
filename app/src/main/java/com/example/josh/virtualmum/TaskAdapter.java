package com.example.josh.virtualmum;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Database.Task.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<Task> taskList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView taskName, taskDue;
        public MyViewHolder(View v) {
            super(v);
            taskName = (TextView) v.findViewById(R.id.taskName);
            taskDue = (TextView) v.findViewById(R.id.taskDue);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Task task = taskList.get(position);
        holder.taskName.setText(task.getName());

//        String date = task.getDueDate();
//        char[] aDate = date.toCharArray();
//        String formattedDate = date.charAt(0)+date.charAt(1)+"/"+aDate[2]+aDate[3]+"/"+aDate[4]+aDate[5]+aDate[6]+aDate[7];


        String dtStart = task.getDueDate();
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
        try {
            Date date = format.parse(dtStart);

            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
            String justDate = targetFormat.format(date);
            holder.taskDue.setText(justDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }





    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return taskList.size();
    }

}
