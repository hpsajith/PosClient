package com.ites.pos.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ites.pos.Adapters.ListItemAdapter;
import com.ites.pos.Adapters.PunchedItemAdapter;
import com.ites.pos.Interfaces.GuestFragmentType;
import com.ites.pos.Interfaces.ListItemType;
import com.ites.pos.MainActivity;
import com.ites.pos.Models.Item;
import com.ites.pos.Models.OrderDetails;
import com.ites.pos.Models.RestaurantItem;
import com.ites.pos.NetworkController;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemPunching extends AppCompatActivity {
    private Map<String, Set<String>> familyMasterMap = new HashMap<>();
    private Map<String, List<RestaurantItem>> masterItemMap = new HashMap<>();
    private List<String> currentFamilyList = new ArrayList<>();
    private List<String> currentMasterList = new ArrayList<>();
    private List<RestaurantItem> currentItemList = new ArrayList<>();
    private View previousTouchedViewMaster, previousTouchedViewItem, previousTouchedViewFamily;
    private Drawable inBuiltBg, previousBg;
    private Item pickedItem;
    private int pickedItemPosition;
    private View previousPickedItem;
    private JSONObject genericObj;
    private OrderDetails orderDetails = new OrderDetails();
    private List<Item> punchedItemsBucket = new ArrayList<>();
    private ArrayAdapter<Item> punchedItemsAdapter;
    private TextView[] billDisplayAttr = new TextView[5];
    private int taxClass;
    private int kotNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_punching);

        final Button rMarkBtn = (Button) findViewById(R.id.remarkBtn);
        final Button enterBtn = (Button) findViewById(R.id.enterBtn);

        if (punchedItemsBucket.isEmpty()) {
            disableBtn(rMarkBtn, false);
            disableBtn(enterBtn, false);
        }

        // bill summary from punchedItemsBucket
        billDisplayAttr[0] = (TextView) findViewById(R.id.totalVal);
        billDisplayAttr[1] = (TextView) findViewById(R.id.totalTax);
        billDisplayAttr[2] = (TextView) findViewById(R.id.discount);
        billDisplayAttr[3] = (TextView) findViewById(R.id.totalPayment);
        billDisplayAttr[4] = (TextView) findViewById(R.id.billAmount);

        // setting guest details
        // generic
        TextView kotNoLabel = (TextView) findViewById(R.id.kotNo);
        final TextView guestName = (TextView) findViewById(R.id.guestName);
        final TextView adultsCount = (TextView) findViewById(R.id.adultCount);
        final TextView kidsCount = (TextView) findViewById(R.id.kidsCount);
        // room related
        final TextView roomNo = (TextView) findViewById(R.id.roomNo);
        TextView foodPackage = (TextView) findViewById(R.id.foodPackage);

        // amount adjuster
        final TextView amountAdjustDisplay = (TextView) findViewById(R.id.display);

        Intent i = getIntent();
        String restItemsStr = i.getStringExtra("restaurantItems");

        try {
            genericObj = new JSONObject(i.getStringExtra("genericObj"));

            JSONArray restItemsSet = new JSONArray(restItemsStr);

            for (int j = 0; j < restItemsSet.length(); j++) {
                RestaurantItem item = new RestaurantItem(restItemsSet.getJSONObject(j));

                if (j == 0) {
                    kotNo = restItemsSet.getJSONObject(j).getInt("kotNo");
                }

                if (!familyMasterMap.containsKey(item.getFamilyName())) {
                    Set<String> set = new HashSet<>();
                    set.add(item.getMasterName());

                    familyMasterMap.put(item.getFamilyName(), set);
                } else {
                    familyMasterMap.get(item.getFamilyName()).add(item.getMasterName());
                }

                if (!masterItemMap.containsKey(item.getMasterName())) {
                    List<RestaurantItem> list = new ArrayList<>();
                    list.add(item);

                    masterItemMap.put(item.getMasterName(), list);
                } else {
                    masterItemMap.get(item.getMasterName()).add(item);
                }
            }

            // set order details
            orderDetails.setKotNo(kotNo);
            orderDetails.setWaiterName(genericObj.getString("waiterName"));
            orderDetails.setTableName(genericObj.getString("tableName"));
            orderDetails.setAdultCount(Integer.valueOf(genericObj.getString("adultsCount")));
            orderDetails.setKidsCount(Integer.valueOf(genericObj.getString("kidsCount")));
            // set date
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            orderDetails.setDate(sdf.format(date));
            if (genericObj.getInt("guestType") == GuestFragmentType.ROOM_LIST) {
                orderDetails.setGuestFName(genericObj.getString("guestFname"));
                orderDetails.setGuestLName(genericObj.getString("guestLname"));
            } else if (genericObj.getInt("guestType") == GuestFragmentType.MANAGER_LIST) {
                String[] name = genericObj.getString("hAccountName").trim().split(" ");
                if (name.length > 1) {
                    orderDetails.setGuestFName(name[0]);
                    orderDetails.setGuestLName(name[1]);
                } else if (name.length == 1) {
                    orderDetails.setGuestFName(name[0]);
                    orderDetails.setGuestLName("");
                } else {
                    orderDetails.setGuestFName("");
                    orderDetails.setGuestLName("");
                }
            } else {
                orderDetails.setGuestFName("");
                orderDetails.setGuestLName("");
            }

            // guest details header
            kotNoLabel.append(" " + orderDetails.getKotNo());
            adultsCount.append(" " + orderDetails.getAdultCount());
            kidsCount.append(" " + orderDetails.getKidsCount());
            if (orderDetails.getGuestFName().equals("") && orderDetails.getGuestLName().equals("")) {
                guestName.append("N/A");
            } else if (orderDetails.getGuestFName().equals("")) {
                guestName.append(orderDetails.getGuestLName());
            } else if (orderDetails.getGuestLName().equals("")) {
                guestName.append(orderDetails.getGuestFName());
            } else {
                guestName.append(orderDetails.getGuestFName() + " " + orderDetails.getGuestLName());
            }
            if (genericObj.getInt("guestType") == GuestFragmentType.ROOM_LIST) {
                roomNo.append(" " + genericObj.getString("reservRoomNo"));
                foodPackage.append(" " + genericObj.getString("foodPackage").trim());
            } else {
                roomNo.append(" N/A");
                foodPackage.append(" N/A");
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        ListView family = (ListView) findViewById(R.id.family);
        final ListView master = (ListView) findViewById(R.id.master);
        final ListView items = (ListView) findViewById(R.id.item);

        currentFamilyList.addAll(familyMasterMap.keySet());
        Collections.sort(currentFamilyList);

        final ListItemAdapter familyAdapter = new ListItemAdapter(this, R.layout.family_list_item, ListItemType.FAMILY, currentFamilyList);

        family.setAdapter(familyAdapter);

        family.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (previousTouchedViewFamily != view) {
                    inBuiltBg = view.getBackground();
                } else {
                    inBuiltBg = previousBg;
                }

                // remove items view
                items.setVisibility(View.INVISIBLE);

                currentMasterList.clear();
                currentMasterList.addAll(familyMasterMap.get(currentFamilyList.get(i)));
                Collections.sort(currentMasterList);

                ListItemAdapter masterAdapter = new ListItemAdapter(getApplicationContext(), R.layout.list_item, ListItemType.MASTER, currentMasterList);

                master.setAdapter(masterAdapter);

                master.setVisibility(View.VISIBLE);
                master.setBackground(inBuiltBg);
            }
        });

        master.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentItemList.clear();
                currentItemList.addAll(masterItemMap.get(currentMasterList.get(i)));

                // sort item list
                Collections.sort(currentItemList, new Comparator<RestaurantItem>() {
                    @Override
                    public int compare(RestaurantItem o1, RestaurantItem o2) {
                        return o1.getItemName().compareTo(o2.getItemName());
                    }
                });

                Iterator ptr = currentItemList.iterator();
                List<String> currentItemListStr = new ArrayList<>();

                while (ptr.hasNext()) {
                    currentItemListStr.add(((RestaurantItem) ptr.next()).getItemName());
                }

                // sort items list names
                Collections.sort(currentItemListStr);

                ListItemAdapter itemsAdapter = new ListItemAdapter(getApplicationContext(), R.layout.list_item, ListItemType.ITEM, currentItemListStr);
                items.setAdapter(itemsAdapter);

                view.setBackground(new ColorDrawable(Color.GRAY));
                items.setVisibility(View.VISIBLE);
                items.setBackground(inBuiltBg);

                if ((previousTouchedViewMaster != null) && (previousTouchedViewMaster != view)) {
                    previousTouchedViewMaster.setBackground(inBuiltBg);
                }
                previousTouchedViewMaster = view;
            }
        });

        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                Drawable inBuiltBg = view.getBackground();

                view.setBackground(new ColorDrawable(Color.GRAY));

                if ((previousTouchedViewItem != null) && (previousTouchedViewItem != view)) {
                    previousTouchedViewItem.setBackground(inBuiltBg);
                }
                previousTouchedViewItem = view;

                final View amountInLayout = getLayoutInflater().inflate(R.layout.amount_input, null);
                final ViewGroup rootView = (ViewGroup) view.getRootView();
                rootView.addView(amountInLayout);
                amountInLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

                final TextView amountInput = (TextView) amountInLayout.findViewById(R.id.amount);
                amountInput.requestFocus();

                final InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                // forch keyboard popup with 500mills delay
                amountInput.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        im.showSoftInput(amountInput, InputMethodManager.SHOW_IMPLICIT);
                    }
                }, 500);

                // hide keyboard when out of focus
                amountInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        im.hideSoftInputFromWindow(amountInput.getWindowToken(), 0);
                    }
                });

                TextView itemNameLabel = (TextView) amountInLayout.findViewById(R.id.itemNamePunch);
                ImageButton close = (ImageButton) amountInLayout.findViewById(R.id.closeBtn_Punch);
                Button punchBtn = (Button) amountInLayout.findViewById(R.id.punchBtn);

                final String itemName = currentItemList.get(i).getItemName();
                final Double itemPrice = currentItemList.get(i).getdUnitPrice();
                final Double itemTaxPrice = currentItemList.get(i).getdTaxPrice();
                final int itemCode = currentItemList.get(i).getItemCode();
                final int currencyId = currentItemList.get(i).getCurrencyId();

                try {
                    if (genericObj.getInt("guestType") == GuestFragmentType.MANAGER_LIST) {
                        taxClass = currentItemList.get(i).getmTaxClass();
                    } else {
                        taxClass = currentItemList.get(i).getDtaxClass();
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


                itemNameLabel.setText(itemName);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rootView.removeView(amountInLayout);
                    }
                });

                punchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amount = String.valueOf(amountInput.getText());

                        if (!amount.trim().equals("") && !amount.trim().equals("0")) {
                            Item j = new Item();
                            j.setItemName(itemName);
                            j.setQty(Integer.valueOf(amount));
                            j.setItemPrice(itemPrice);
                            j.setTaxPrice(itemTaxPrice);
                            j.setItemCode(itemCode);
                            j.setTaxClass(taxClass);
                            j.setCurrencyId(currencyId);

                            punchedItemsBucket.add(j);
                            punchedItemsAdapter.notifyDataSetChanged();
                            billSummaryRefresh();

                            // enable buttons
                            disableBtn(enterBtn, true);
                            disableBtn(rMarkBtn, true);

                            // dismiss input
                            rootView.removeView(amountInLayout);
                        }
                    }
                });
            }
        });

        // punched items list
        ListView punchedItems = (ListView) findViewById(R.id.billItems);

        punchedItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable inBuilt = view.getBackground();

                view.setBackground(new ColorDrawable(Color.GRAY));

                pickedItemPosition = position;
                pickedItem = punchedItemsBucket.get(position);

                amountAdjustDisplay.setText(punchedItemsBucket.get(position).getQty() + "");

                if ((previousPickedItem != null) && (previousPickedItem != view)) {
                    previousPickedItem.setBackground(inBuilt);
                }

                previousPickedItem = view;
            }
        });

        punchedItemsAdapter = new PunchedItemAdapter(getApplicationContext(), R.layout.punched_item, punchedItemsBucket);
        punchedItems.setAdapter(punchedItemsAdapter);

        // exit action
        Button exitBtn = (Button) findViewById(R.id.exitBtn);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(ItemPunching.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });

        rMarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickedItem != null) {
                    addComment(v);
                } else {
                    showAlert(v, "You havn't picked any item!");
                }
            }
        });

        // bill summary init
        billSummaryRefresh();


        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkController netCtrl = new NetworkController(getApplicationContext());

                netCtrl.placeGuestOrder(punchedItemsBucket, orderDetails);

                showAlert(v, "Order Placed!");
            }
        });

        ImageButton add = (ImageButton) findViewById(R.id.add);
        ImageButton remove = (ImageButton) findViewById(R.id.remove);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((pickedItem != null) && !amountAdjustDisplay.getText().equals("0")) {
                    addItems();
                    amountAdjustDisplay.setText(punchedItemsBucket.get(pickedItemPosition).getQty() + "");
                    billSummaryRefresh();
                } else {
                    showAlert(v, "You havn't picked any item!");
                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((pickedItem != null) && !amountAdjustDisplay.getText().equals("0")) {
                    removeItems();
                    billSummaryRefresh();
                    if (pickedItemPosition < punchedItemsBucket.size()) {
                        amountAdjustDisplay.setText(punchedItemsBucket.get(pickedItemPosition).getQty() + "");
                    } else {
                        amountAdjustDisplay.setText("0");
                        // if and only if bucket is empty
                        if (punchedItemsBucket.isEmpty()) {
                            disableBtn(rMarkBtn, false);
                            disableBtn(enterBtn, false);
                        }
                    }
                } else {
                    showAlert(v, "You havn't picked any item!");
                }
            }
        });

        ImageButton editOrderDetails = (ImageButton) findViewById(R.id.editOrderDetails);

        editOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View editOrderView = getLayoutInflater().inflate(R.layout.edit_order_details, null);
                editOrderView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                final ViewGroup rootView = (ViewGroup) v.getRootView();

                rootView.addView(editOrderView);

                // edit order view elements
                ImageButton closeBtn = (ImageButton) editOrderView.findViewById(R.id.closeBtn_editOrder);
                Button save = (Button) editOrderView.findViewById(R.id.saveOrderEdit);
                final EditText guestFname = (EditText) editOrderView.findViewById(R.id.guestFName);
                final EditText guestLname = (EditText) editOrderView.findViewById(R.id.guestLName);
                final EditText adultsCountEdit = (EditText) editOrderView.findViewById(R.id.adultsCountEdit);
                final EditText kidsCountEdit = (EditText) editOrderView.findViewById(R.id.kidsCountEdit);
                final EditText tableName = (EditText) editOrderView.findViewById(R.id.tableNameEdit);
                final EditText waiterName = (EditText) editOrderView.findViewById(R.id.waiterName);
                final EditText date = (EditText) editOrderView.findViewById(R.id.date);
                final EditText remark = (EditText) editOrderView.findViewById(R.id.remarkEdit);

                // set vals for edit order view elements
                guestFname.setText(orderDetails.getGuestFName());
                guestLname.setText(orderDetails.getGuestLName());
                adultsCountEdit.setText(orderDetails.getAdultCount() + "");
                kidsCountEdit.setText(orderDetails.getKidsCount() + "");
                tableName.setText(orderDetails.getTableName());
                waiterName.setText(orderDetails.getWaiterName());
                date.setText(orderDetails.getDate());
                remark.setText(orderDetails.getRemark());

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rootView.removeView(editOrderView);
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderDetails.setGuestFName(guestFname.getText().toString().trim());
                        orderDetails.setGuestLName(guestLname.getText().toString().trim());
                        orderDetails.setAdultCount(Integer.valueOf(adultsCountEdit.getText().toString()));
                        orderDetails.setKidsCount(Integer.valueOf(kidsCountEdit.getText().toString()));
                        orderDetails.setTableName(tableName.getText().toString().trim());
                        orderDetails.setWaiterName(waiterName.getText().toString().trim());
                        orderDetails.setDate(date.getText().toString().trim());
                        orderDetails.setRemark(remark.getText().toString().trim());

                        // refresh vals
                        if (orderDetails.getGuestFName().equals("") && orderDetails.getGuestLName().equals("")) {
                            guestName.setText("Guest Name: N/A");
                        } else if (orderDetails.getGuestFName().equals("")) {
                            guestName.setText("Guest Name: " + orderDetails.getGuestLName());
                        } else if (orderDetails.getGuestLName().equals("")) {
                            guestName.setText("Guest Name: " + orderDetails.getGuestFName());
                        } else {
                            guestName.setText("Guest Name: " + orderDetails.getGuestFName() + " " + orderDetails.getGuestLName());
                        }
                        adultsCount.setText("Adults Count: " + orderDetails.getAdultCount());
                        kidsCount.setText("Kids Count: " + orderDetails.getKidsCount());

                        showAlert(v, "Details saved!");
                        rootView.removeView(editOrderView);
                    }
                });
            }
        });
    }

    private void removeItems() {
        int qty = punchedItemsBucket.get(pickedItemPosition).getQty();
        qty--;

        if (qty > 0) {
            punchedItemsBucket.get(pickedItemPosition).setQty(qty);
        } else {
            punchedItemsBucket.remove(pickedItemPosition);
        }
        punchedItemsAdapter.notifyDataSetChanged();
    }

    private void addItems() {
        int qty = punchedItemsBucket.get(pickedItemPosition).getQty();
        qty++;
        punchedItemsBucket.get(pickedItemPosition).setQty(qty);
        punchedItemsAdapter.notifyDataSetChanged();
    }


    private void disableBtn(Button btn, boolean bool) {
        btn.setEnabled(bool);
        if (bool) {
            btn.setBackground(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        } else {
            btn.setBackground(new ColorDrawable(Color.GRAY));
        }
    }

    private void showAlert(View v, String msg) {
        Snackbar alert = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        TextView tv = (TextView) alert.getView().findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= 17) {
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setGravity(Gravity.CENTER);
        }

        alert.show();
    }

    private void addComment(View v) {
        final ViewGroup rootView = (ViewGroup) v.getRootView();
        final View rMarkLayout = getLayoutInflater().inflate(R.layout.remark_layout, null);
        rootView.addView(rMarkLayout);
        rMarkLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        TextView itemName = (TextView) rMarkLayout.findViewById(R.id.itemNameRemark);

        itemName.setText(pickedItem.getItemName());

        final TextView comment = (TextView) rMarkLayout.findViewById(R.id.rMarkComment);
        comment.requestFocus();

        Button addComment = (Button) rootView.findViewById(R.id.addComment);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentStr = String.valueOf(comment.getText());

                if (!commentStr.trim().equals("")) {
                    // set comment
                    pickedItem.setComment(commentStr);
                    showAlert(v, "Comment Added!");

                    rootView.removeView(rMarkLayout);
                } else {
                    showAlert(v, "Comment Box is Empty!");
                }
            }
        });

        final InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                im.hideSoftInputFromWindow(comment.getWindowToken(), 0);
            }
        });

        // forch keyboard popup with 500mills delay
        comment.postDelayed(new Runnable() {
            @Override
            public void run() {
                im.showSoftInput(comment, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);

        ImageButton closeBtn = (ImageButton) rootView.findViewById(R.id.closeBtn_rMark);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.removeView(rMarkLayout);
            }
        });
    }

    private void billSummaryRefresh() {
        if (!punchedItemsBucket.isEmpty()) {
            Double[] attrs = {0.00, 0.00, 0.00, 0.00, 0.00}; //totalVal, totalTax, discount, totalPayment, billAmount
            Iterator<Item> i = punchedItemsBucket.iterator();

            while (i.hasNext()) {
                Item j = i.next();
                attrs[0] += (j.getItemPrice() * j.getQty()); // total value
                attrs[3] += (j.getTaxPrice() * j.getQty()); // total value with tax(totalPayment)
            }
            attrs[1] = attrs[3] - attrs[0]; // total tax
            attrs[4] = attrs[3] - attrs[2]; // bill amount

            attrs[0] = Math.round(attrs[0] * 100) / 100.00;
            attrs[1] = Math.round(attrs[1] * 100) / 100.00;
            attrs[2] = Math.round(attrs[2] * 100) / 100.00;
            attrs[3] = Math.round(attrs[3] * 100) / 100.00;
            attrs[4] = Math.round(attrs[4] * 100) / 100.00;

            billDisplayAttr[0].setText(attrs[0] + "");
            billDisplayAttr[1].setText(attrs[1] + "");
            billDisplayAttr[2].setText(attrs[2] + "");
            billDisplayAttr[3].setText(attrs[3] + "");
            billDisplayAttr[4].setText(attrs[4] + "");
        } else {
            billDisplayAttr[0].setText("0.00");
            billDisplayAttr[1].setText("0.00");
            billDisplayAttr[2].setText("0.00");
            billDisplayAttr[3].setText("0.00");
            billDisplayAttr[4].setText("0.00");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null
                && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && v instanceof EditText
                && !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                dismissKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void dismissKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    // disable back button
    @Override
    public void onBackPressed() {
    }
}