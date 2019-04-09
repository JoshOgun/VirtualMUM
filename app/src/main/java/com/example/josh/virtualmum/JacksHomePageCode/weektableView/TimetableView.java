package com.example.josh.virtualmum.JacksHomePageCode.weektableView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.josh.virtualmum.R;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TimetableView extends LinearLayout {
    private static final int DEFAULT_ROW_COUNT =17;
    private static final int DEFAULT_COLUMN_COUNT = 8;
    private static final int DEFAULT_CELL_HEIGHT_DP = 50;
    private static final int DEFAULT_SIDE_CELL_WIDTH_DP = 30;
    private static final int DEFAULT_START_TIME = 7;

    private static final int DEFAULT_SIDE_HEADER_FONT_SIZE_DP = 13;
    private static final int DEFAULT_HEADER_FONT_SIZE_DP = 15;
    private static final int DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP = 15;
    private static final int DEFAULT_STICKER_FONT_SIZE_DP = 11;


    private int rowCount;
    private int columnCount;
    private int cellHeight;
    private int sideCellWidth;
    private String[] headerTitle;
    private String[] stickerColors;
    private int startTime;
    private int headerHighlightColor;

    private RelativeLayout stickerBox;
    TableLayout tableHeader;
    TableLayout tableBox;

    private Context context;

    HashMap<Integer, Sticker> stickers = new HashMap<Integer, Sticker>();
    private int stickerCount = -1;

    private OnStickerSelectedListener stickerSelectedListener = null;

    public TimetableView(Context context) {
        super(context, null);
        this.context = context;
    }

    public TimetableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimetableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getAttrs(attrs);
        init();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, com.example.josh.virtualmum.R.styleable.TimetableView);
        rowCount = a.getInt(com.example.josh.virtualmum.R.styleable.TimetableView_row_count, DEFAULT_ROW_COUNT) - 1;
        columnCount = a.getInt(com.example.josh.virtualmum.R.styleable.TimetableView_column_count, DEFAULT_COLUMN_COUNT);
        cellHeight = a.getDimensionPixelSize(com.example.josh.virtualmum.R.styleable.TimetableView_cell_height, dp2Px(DEFAULT_CELL_HEIGHT_DP));
        sideCellWidth = a.getDimensionPixelSize(com.example.josh.virtualmum.R.styleable.TimetableView_side_cell_width, dp2Px(DEFAULT_SIDE_CELL_WIDTH_DP));
        int colorsId = a.getResourceId(com.example.josh.virtualmum.R.styleable.TimetableView_sticker_colors,com.example.josh.virtualmum.R.array.default_sticker_color);
        stickerColors = a.getResources().getStringArray(colorsId);
        startTime = a.getInt(com.example.josh.virtualmum.R.styleable.TimetableView_start_time, DEFAULT_START_TIME);
        headerHighlightColor = a.getColor(com.example.josh.virtualmum.R.styleable.TimetableView_header_highlight_color, getResources().getColor(com.example.josh.virtualmum.R.color.default_header_highlight_color));
        a.recycle();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_weektable, this, false);
        addView(view);

        stickerBox = view.findViewById(com.example.josh.virtualmum.R.id.sticker_box1);
        tableHeader = view.findViewById(com.example.josh.virtualmum.R.id.table_header);
        tableBox = view.findViewById(com.example.josh.virtualmum.R.id.table_box1);

        createTable();
    }




    public void add(ArrayList<Schedule> schedules) {
        add(schedules, -1);
    }

    private void add(final ArrayList<Schedule> schedules, int specIdx) {
        final int count = specIdx < 0 ? ++stickerCount : specIdx;
        Sticker sticker = new Sticker();
        for (Schedule schedule : schedules) {
            TextView tv = new TextView(context);

            RelativeLayout.LayoutParams param = createStickerParam(schedule);
            tv.setLayoutParams(param);
            tv.setPadding(10, 0, 10, 0);
            tv.setText(schedule.getClassTitle() + "\n" + schedule.getClassPlace()+"\n"+schedule.getProfessorName());
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STICKER_FONT_SIZE_DP);
            tv.setTypeface(null, Typeface.BOLD);

            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stickerSelectedListener != null)
                        stickerSelectedListener.OnStickerSelected(count, schedules);
                }
            });

            sticker.addTextView(tv);
            sticker.addSchedule(schedule);
            stickers.put(count, sticker);
            stickerBox.addView(tv);

        }

    }




    public void removeAll() {
        for (int key : stickers.keySet()) {
            Sticker sticker = stickers.get(key);
            for (TextView tv : sticker.getView()) {
                stickerBox.removeView(tv);
            }
        }
        stickers.clear();
    }

  //  public void edit(int idx, ArrayList<Schedule> schedules) {
    //    remove(idx);
      //  add(schedules, idx);
   // }

  //  public void remove(int idx) {
    //    Sticker sticker = stickers.get(idx);
      //  for (TextView tv : sticker.getView()) {
        //    stickerBox.removeView(tv);
        //}
       // stickers.remove(idx);
        //setStickerColor();
    //}

    public void setHeaderHighlight(int idx) {
        TableRow row = (TableRow) tableHeader.getChildAt(0);
        TextView tx = (TextView) row.getChildAt(idx);
        tx.setTextColor(Color.parseColor("#FFFFFF"));
        tx.setBackgroundColor(headerHighlightColor);
        tx.setTypeface(null, Typeface.BOLD);
        tx.setTextSize(TypedValue.COMPLEX_UNIT_DIP,DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP);
    }

    public void setStickerColor(int x) {
        int size = stickers.size();
        int[] orders = new int[size];
        int i = 0;
        for (int key : stickers.keySet())
        { orders[i++] = key;

        }
        Arrays.sort(orders);

        //int colorSize = stickerColors.length;

        for (i = 0; i < size; i++)
        for (TextView v : stickers.get(orders[orders.length-1]).getView()) {
                v.setBackgroundColor(x);
        }


    }

    private void createTable() {
       createTableHeader();
        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(createTableLayoutParam());

            for (int k = 0; k < columnCount; k++) {
                TextView tv = new TextView(context);
                tv.setLayoutParams(createTableRowParam(cellHeight));
                if (k == 0) {
                    tv.setText(getHeaderTime(i));
                    tv.setTextColor(getResources().getColor(R.color.colorHeaderText));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SIDE_HEADER_FONT_SIZE_DP);
                    tv.setBackgroundColor(getResources().getColor(R.color.colorHeader));
                    tv.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                    tv.setLayoutParams(createTableRowParam(sideCellWidth, cellHeight));
                } else {
                    tv.setText("");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv.setBackground(getResources().getDrawable(R.drawable.item_border));
                    }
                    tv.setGravity(Gravity.RIGHT);
                }
                tableRow.addView(tv);
            }
            tableBox.addView(tableRow);
        }
    }

    private void createTableHeader() {
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(createTableLayoutParam());
        Date date = new Date();

        for (int i = 0; i < columnCount; i++) {
            TextView tv = new TextView(context);
            if (i == 0) {
                tv.setLayoutParams(createTableRowParam(sideCellWidth, cellHeight));
            } else {
                tv.setLayoutParams(createTableRowParam(cellHeight));
            }
            tv.setTextColor(getResources().getColor(com.example.josh.virtualmum.R.color.colorHeaderText));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_FONT_SIZE_DP);
            if (i != 0){
            tv.setText(getWeekOfDate(date,i-1));}
            else
                tv.setText(" ");
            tv.setGravity(Gravity.CENTER);

            tableRow.addView(tv);
        }
        tableHeader.addView(tableRow);
    }

    public String getWeekOfDate(Date date,int i) {
        String[] weekDays = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        w = w+i;
        if (w >=7)
          w=w-7;
        return weekDays[w];
    }

    private RelativeLayout.LayoutParams createStickerParam(Schedule schedule) {
        int cell_w = calCellWidth();

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(cell_w, calStickerHeightPx(schedule));
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.setMargins(sideCellWidth + cell_w * schedule.getDay(), calStickerTopPxByTime(schedule.getStartTime()), 0, 0);

        return param;
    }

    private int calCellWidth(){
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int cell_w = (size.x-getPaddingLeft() - getPaddingRight()- sideCellWidth) / (columnCount - 1);
        return cell_w;
    }

    private int calStickerHeightPx(Schedule schedule) {
        int startTopPx = calStickerTopPxByTime(schedule.getStartTime());
        int endTopPx = calStickerTopPxByTime(schedule.getEndTime());
        int d = endTopPx - startTopPx;

        return d;
    }

    private int calStickerTopPxByTime(Time time) {
        int topPx = (time.getHour() - startTime) * cellHeight + (int) ((time.getMinute() / 60.0f) * cellHeight);
        return topPx;
    }

    private TableLayout.LayoutParams createTableLayoutParam() {
        return new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
    }

    private TableRow.LayoutParams createTableRowParam(int h_px) {
        return new TableRow.LayoutParams(calCellWidth(), h_px);
    }

    private TableRow.LayoutParams createTableRowParam(int w_px, int h_px) {
        return new TableRow.LayoutParams(w_px, h_px);
    }

    private String getHeaderTime(int i) {

        int p =  startTime + i % 24 ;
        String t = " ";
        if(p<=12){
            t= String.valueOf(p)+"\n"+  "A"+ "M";
        }
        else {
            t = String.valueOf(p-12)+"\n"+  "P"+ "M";
        }

        return t;
    }
    static private int dp2Px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }




    public interface OnStickerSelectedListener {
        void OnStickerSelected(int idx, ArrayList<Schedule> schedules);
    }



}