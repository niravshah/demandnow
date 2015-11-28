package com.demandnow.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demandnow.R;
import com.demandnow.model.ContactInfo;

import java.util.List;

/**
 * Created by Nirav on 28/11/2015.
 */
public class ServiceSelectorAdapter extends RecyclerView.Adapter<ServiceSelectorAdapter.ContactViewHolder> {

    private List<ContactInfo> contactList;

    public ServiceSelectorAdapter(List<ContactInfo> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ContactInfo ci = contactList.get(i);
        contactViewHolder.vName.setText(ci.name);
        contactViewHolder.vSurname.setText(ci.surname);
        contactViewHolder.vEmail.setText(ci.email);
        contactViewHolder.vTitle.setText(ci.name + " " + ci.surname);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardv_service_selector, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vSurname;
        protected TextView vEmail;
        protected TextView vTitle;

        public ContactViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView) v.findViewById(R.id.txtSurname);
            vEmail = (TextView) v.findViewById(R.id.txtEmail);
            vTitle = (TextView) v.findViewById(R.id.title);
        }
    }
}
