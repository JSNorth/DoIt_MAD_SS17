package de.fh_luebeck.jsn.doit.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import de.fh_luebeck.jsn.doit.R;
import de.fh_luebeck.jsn.doit.data.AssociatedContact;
import de.fh_luebeck.jsn.doit.interfaces.ContactEvents;

/**
 * Created by USER on 03.07.2017.
 */

public class ContactAdapter extends ArrayAdapter<AssociatedContact> {

    private List<AssociatedContact> mItems;
    private final Context mContext;
    private final LayoutInflater inflater;
    private final ContactEvents eventHandler;

    public ContactAdapter(Context context, int resource, List<AssociatedContact> associatedContacts, ContactEvents eventHandler) {
        super(context, resource);

        mContext = context;
        mItems = associatedContacts;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.eventHandler = eventHandler;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.associated_list_entry, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.contact_name);
            holder.smsButton = (ImageButton) convertView.findViewById(R.id.sms_button);
            holder.eMailButton = (ImageButton) convertView.findViewById(R.id.email_button);
            holder.removeButton = (ImageButton) convertView.findViewById(R.id.remove_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AssociatedContact item = getItem(position);
        if (item != null) {
            holder.text.setText(item.getName());

            if (item.geteMail() == null || item.geteMail().isEmpty() == true) {
                holder.eMailButton.setVisibility(View.GONE);
            }

            if (item.getMobile() == null || item.getMobile().isEmpty() == true) {
                holder.smsButton.setVisibility(View.GONE);
            }

            holder.smsButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            eventHandler.sendMessage(position);
                        }
                    }
            );

            holder.eMailButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            eventHandler.sendEMail(position);
                        }
                    }
            );

            holder.removeButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            eventHandler.removeContact(position);
                        }
                    }
            );
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public AssociatedContact getItem(int position) {
        return mItems.get(position);
    }

    public class ViewHolder {
        TextView text;
        ImageButton smsButton;
        ImageButton eMailButton;
        ImageButton removeButton;
    }

    public void updateData(List<AssociatedContact> contacts) {
        this.mItems = contacts;
        notifyDataSetChanged();
    }
}
