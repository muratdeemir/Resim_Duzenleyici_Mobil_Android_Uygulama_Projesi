package com.ss.resimduzenleyici.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.resimduzenleyici.R;
import com.ss.resimduzenleyici.activities.EditorActivity;
import com.ss.resimduzenleyici.asynctasks.PreviewThumbAsyncTask;
import com.ss.resimduzenleyici.utils.AsyncTaskUtils;
import com.ss.resimduzenleyici.widget.BaseToolObject;
import com.ss.resimduzenleyici.widget.EffectToolObject;
import com.ss.resimduzenleyici.widget.OptimizeToolObject;

import java.util.List;

public class EditorListToolsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<BaseToolObject> data;


    public EditorListToolsAdapter(Context context, List<BaseToolObject> data) {
        this.context = context;
        this.data = data;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (this.data == null)
            return 0;
        else return this.data.size();
    }

    @Override
    public BaseToolObject getItem(int position) {
        if (this.data == null) {
            return null;
        } else {
            return this.data.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ui_list_tool_item, parent, false);
            holder = new ViewHolder();
            holder.toolIcon = (ImageView) convertView.findViewById(R.id.transformIcon);
            holder.toolName = (TextView) convertView.findViewById(R.id.transformName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BaseToolObject obj = this.data.get(position);
        if (obj instanceof EffectToolObject) {

            // holder.toolIcon.setImageResource(obj.iconResourceId);
            PreviewThumbAsyncTask thumbAsyncTask = new PreviewThumbAsyncTask.Builder(
                    holder.toolIcon,
                    obj,
                    ((EditorActivity) context).getThumbnail())
                    .setOnProcessedListener(new PreviewThumbAsyncTask.OnProcessedListener() {
                        @Override
                        public void onProcessed() {

                        }
                    })
                    .build();
            AsyncTaskUtils.executeTask(thumbAsyncTask);
        } else if (obj instanceof OptimizeToolObject) {
            holder.toolIcon.setImageResource(obj.iconResourceId);
        } else if (obj != null) {
            holder.toolIcon.setImageResource(obj.iconResourceId);
        }

        assert obj != null;
        holder.toolName.setText(obj.name);
        return convertView;
    }

    private static class ViewHolder {
        ImageView toolIcon;
        TextView toolName;
    }

}
