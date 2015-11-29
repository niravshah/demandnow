package com.demandnow.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demandnow.MainActivity;
import com.demandnow.R;
import com.demandnow.SharedPrefrences;
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
        contactViewHolder.info = ci;
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
        protected ContactInfo info;

        public ContactViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.txtName);

            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    SharedPrefrences.setCurrentService(info.getName());
                    v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                }
            });
        }
    }
}