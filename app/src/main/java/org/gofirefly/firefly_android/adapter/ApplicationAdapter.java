package org.gofirefly.firefly_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.gofirefly.firefly_android.R;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by LaContra on 16/1/17.
 */
public class ApplicationAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        private String[] applications = {"Facebook", "Chrome", "Safari", "Twitter",
                "Line", "Pinterest", "Uber", "FireFox",
                "Evernote", "Lyft", "Venmo", "Belly"};
        private LayoutInflater inflater;

        public ApplicationAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return applications.length;
        }

        @Override
        public Object getItem(int position) {
            return applications[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_application_item, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.application_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(applications[position]);

            return convertView;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.list_application_section, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.section_title);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            //set header text as first char in name
            String headerText = "" + applications[position].subSequence(0, 1).charAt(0);
            holder.text.setText(headerText);
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            //return the first character of the country as ID because this is what headers are based upon
            return applications[position].subSequence(0, 1).charAt(0);
        }

        class HeaderViewHolder {
            TextView text;
        }

        class ViewHolder {
            TextView text;
        }

    }
