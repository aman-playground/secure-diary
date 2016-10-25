package com.ajain.securediary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


class PasswordListAdapter extends ArrayAdapter {
    private ArrayList<Entry> dataEntries;
    private final int resourceId;
    private final LayoutInflater inflater;

    PasswordListAdapter(Context context, int resource, ArrayList<Entry> dataEntries) {
        super(context, resource);
        this.dataEntries = dataEntries;
        this.resourceId = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataEntries.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(resourceId, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Entry entry = dataEntries.get(position);
        viewHolder.titleTV.setText(entry.getTitle());
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEntry((int) view.getTag());
            }
        });
        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEntry((int) view.getTag());
            }
        });
        viewHolder.editBtn.setTag(position);
        viewHolder.deleteBtn.setTag(position);
        return convertView;
    }

    private void editEntry(int position) {
        PasswordListActivity passwordListActivity = (PasswordListActivity) super.getContext();
        passwordListActivity.confirmPasswordFor(dataEntries.get(position), "edit");
    }

    private void deleteEntry(int position) {
        PasswordListActivity passwordListActivity = (PasswordListActivity) super.getContext();
        passwordListActivity.confirmPasswordFor(dataEntries.get(position), "delete");
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView titleTV;
        Button deleteBtn;
        Button editBtn;

        ViewHolder(View view) {
            titleTV = (TextView) view.findViewById(R.id.titleTV);
            deleteBtn = (Button) view.findViewById(R.id.deleteBtn);
            editBtn = (Button) view.findViewById(R.id.editBtn);
            titleTV.setText("");
        }
    }
}
