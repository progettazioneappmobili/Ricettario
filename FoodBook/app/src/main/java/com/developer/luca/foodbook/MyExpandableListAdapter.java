package com.developer.luca.foodbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Copyright (C) 2018 Levi Rizki Saputra (levirs565@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by LEVI on 22/09/2018.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private HashMap<String, List<String>> mStringListHashMap;
    private String[] mListHeaderGroup;
    private Character separator = '§';



    public MyExpandableListAdapter(HashMap<String, List<String>> stringListHashMap) {
        mStringListHashMap = stringListHashMap;
        mListHeaderGroup = mStringListHashMap.keySet().toArray(new String[0]);
    }


    @Override
    public int getGroupCount() {
        return mListHeaderGroup.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mStringListHashMap.get(mListHeaderGroup[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mListHeaderGroup[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mStringListHashMap.get(mListHeaderGroup[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition*childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_group, parent, false);

        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(String.valueOf(getGroup(groupPosition)));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item, parent, false);

        String piatti = String.valueOf(getChild(groupPosition, childPosition));

        ArrayList<String> piatti2 = splitStrings(piatti);

        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(piatti2.get(0));

        TextView textView2 = convertView.findViewById(R.id.textView2);
        textView2.setText(piatti2.get(1));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private ArrayList<String> splitStrings(String toSplit){
        // Variabili che conterranno i nomi dei due piatti
        StringBuilder piatto1 = new StringBuilder();
        StringBuilder piatto2 = new StringBuilder();

        Boolean separatorFound = false;

        while(toSplit.length() > 0){
            if(toSplit.charAt(0) == separator){
                separatorFound = true;
                toSplit = toSplit.substring(1);
            }
            if(separatorFound){
                piatto2.append(toSplit.charAt(0));
                toSplit = toSplit.substring(1);
            }else{
                piatto1.append(toSplit.charAt(0));
                toSplit = toSplit.substring(1);
            }
        }

        ArrayList<String> result = new ArrayList<String>();
        result.add(piatto1.toString());
        result.add(piatto2.toString());

        return result;
    }
}
