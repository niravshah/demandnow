package com.demandnow;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.demandnow.adapters.ServiceSelectorAdapter;
import com.demandnow.model.ContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nirav on 27/11/2015.
 */
public class ServiceSelectorActivity extends GDNBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selector);
        renderNavigationDrawer();
        renderChildActivityToolbar();

        RecyclerView rv = (RecyclerView)findViewById(R.id.service_selector_rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        ServiceSelectorAdapter adapter = new ServiceSelectorAdapter(getContactList(10));
        rv.setAdapter(adapter);

    }

    private List<ContactInfo> getContactList(int count) {

        List<ContactInfo> list = new ArrayList<>();
        for(int i=0;i<count;i++){
            list.add(new ContactInfo("Name_"+i,"Sname_"+i,i+"@test.com"));
        }
        return list;
    }


}
