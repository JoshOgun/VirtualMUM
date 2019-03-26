package com.example.josh.virtualmum;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Database.Event.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> eventList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView eventName, EventTiming, location;
        public MyViewHolder(View v) {
            super(v);
            eventName = (TextView) v.findViewById(R.id.eventName);
            EventTiming = (TextView) v.findViewById(R.id.StartTimeToEndTime);
            location = (TextView) v.findViewById(R.id.Location);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Event event = eventList.get(position);
        holder.eventName.setText(event.getName());

//        String date = task.getDueDate();
//        char[] aDate = date.toCharArray();
//        String formattedDate = date.charAt(0)+date.charAt(1)+"/"+aDate[2]+aDate[3]+"/"+aDate[4]+aDate[5]+aDate[6]+aDate[7];


        String dtStart = event.getStartDate();
        String dtEnd = event.getEndDate();

        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
        try {
            Date dateS = format.parse(dtStart);
            Date dateE = format.parse(dtStart);


            SimpleDateFormat dTargetFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            SimpleDateFormat tTargetFormat = new SimpleDateFormat("HH:mm");

            String justDate = dTargetFormat.format(dateS);
            justDate.concat(" - ");
            justDate.concat(tTargetFormat.format(dateE));
            holder.EventTiming.setText(justDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }





    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
