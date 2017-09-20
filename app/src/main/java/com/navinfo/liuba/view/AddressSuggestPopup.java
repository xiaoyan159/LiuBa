package com.navinfo.liuba.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.navinfo.liuba.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class AddressSuggestPopup {
    private PopupWindow suggestPopup;//展示地址建议的popupWidow
    private Context mContext;
    private EditText edt_address;
    private SimpleAdapter addressAdapter;
    private SharedPreferences spf;

    private final String NAME = "name";
    private final String ICON = "icon";
    private final String LAT = "LAT";
    private final String LON = "LON";
    private final String UID = "UID";

    private static AddressSuggestPopup instatnce;
    private SuggestionSearch mSuggestionSearch;

    private List<Map<String, Object>> addressMapList;

    private AddressSuggestPopup() {
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                if (suggestPopup != null) {
                    if (suggestionResult == null) {
                        suggestPopup.dismiss();
                    } else {
                        List<SuggestionResult.SuggestionInfo> suggestionInfoList = suggestionResult.getAllSuggestions();
                        List<Map<String, Object>> listMap = getAddressListMap(suggestionInfoList);
                        if (listMap != null) {
                            addressMapList.clear();
                            addressMapList.addAll(listMap);
                            addressAdapter.notifyDataSetChanged();
                            suggestPopup.showAsDropDown(edt_address);
                        }
                    }
                }
            }
        });
    }

    public static AddressSuggestPopup getInstance() {
        if (instatnce == null) {
            instatnce = new AddressSuggestPopup();
        }
        return instatnce;
    }

    public void addSuggestEditText(Context mContext, final EditText edt,final String cityStr) {
        if (mContext != null && edt != null) {
            edt_address = edt;
            suggestPopup = new PopupWindow(mContext);
            suggestPopup.setTouchable(true);
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.list_view_address_suggest, null);
            suggestPopup.setContentView(contentView);
            suggestPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            suggestPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            suggestPopup.setBackgroundDrawable(new ColorDrawable(0x00000000));
            suggestPopup.setOutsideTouchable(true);
            suggestPopup.setFocusable(false);
            ListView lv = (ListView) contentView.findViewById(R.id.lv_poi_address_suggest);
            addressMapList = new ArrayList<Map<String, Object>>();
            addressAdapter = new SimpleAdapter(mContext, addressMapList, R.layout.left_icon_text_card_4_address, new String[]{ICON, NAME}, new int[]{R.id.iv_left_icon_text_card, R.id.tv_left_icon_text_card});
            lv.setAdapter(addressAdapter);
            edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if (suggestPopup != null && suggestPopup.isShowing()) {
                            suggestPopup.dismiss();
                        }
                    }
                }
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (addressMapList != null && !addressMapList.isEmpty() && addressMapList.size() > position) {
                        edt.setText(addressMapList.get(position).get(NAME).toString());
                        edt.setSelection(edt.getText().length());
                        suggestPopup.dismiss();
//                        edt.setTag(SystemConstant.CURRENT_LOCATION,);
                    }
                }
            });

            //等待界面完全加载结束后，设置suggestPopup的宽度
            edt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    edt.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            suggestPopup.setWidth(edt.getMeasuredWidth());
                        }
                    }, 300);
                }
            });

            //用户修改输入框的文字，则自动隐藏popup
            edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s!=null&&s.toString().length()>2){
                        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                                .keyword(s.toString())
                                .city(cityStr));
                    }
                }
            });
        }
    }

    private List<Map<String, Object>> getAddressListMap(List<SuggestionResult.SuggestionInfo> list) {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        if (list != null && !list.isEmpty()) {
            for (SuggestionResult.SuggestionInfo suggestionInfo : list) {
                if (suggestionInfo != null) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(NAME, suggestionInfo.key);
                    map.put(ICON, R.mipmap.icon_address_location);
                    map.put(LAT, suggestionInfo.pt.latitude);
                    map.put(LON, suggestionInfo.pt.longitude);
                    map.put(UID, suggestionInfo.uid);
                    map.put("type", 0);
                    listMap.add(map);
                }
            }
            if (listMap != null && !listMap.isEmpty())
                return listMap;
        }
        return listMap;
    }

    public void dismiss() {
        if (suggestPopup != null) {
            suggestPopup.dismiss();
        }
    }
}
