package com.mervekaradas.kyk.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mervekaradas.kyk.R;
import com.mervekaradas.kyk.model.MenuModel;
import com.mervekaradas.kyk.view.RatingActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowHolder> {

    private ArrayList<MenuModel> menuList;

    private String[] colors =
            {
                    "#FF673AB7"
            };


    public RecyclerViewAdapter(ArrayList<MenuModel> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //row_layout ile recyclerView i bağlamak için
        //Inflater lar oluşturduğumuz farklı xml leri sınıfları bağlamamıza yardımcı olur
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_layout, parent, false);
        return new RowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) { //Görünümlerizi ViewHolder da tanımlayıp verileri dolduruyoruz hangi pozisyonda ne gösterilsin gibi işlemleri içerir
        holder.bind(menuList.get(position), colors, position);
    }

    @Override
    public int getItemCount() { //Kaç tane row oluşturulacağını söylüyor
        return menuList.size();
    }

    public class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtDate;
        TextView textMenuList;
        public RowHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Tıklama gerçekleştiğinde burası çalışacak
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                MenuModel clickedItem = menuList.get(position);
                Toast.makeText(itemView.getContext(), "Clicked: " + clickedItem.getDate(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(view.getContext(), RatingActivity.class);
                intent.putExtra("date", clickedItem.getDate());
                view.getContext().startActivity(intent);
            }
        }

        public void bind(MenuModel model,String[] colors, int position) {

            itemView.setBackgroundColor(Color.parseColor(colors[position % colors.length]));
            txtDate = itemView.findViewById(R.id.txtDate);
            textMenuList = itemView.findViewById(R.id.txtMenuList);
            txtDate.setText(model.getDate());


            StringBuilder menuListText = new StringBuilder();
            List<String> menuList = model.getMenuList();
            for (String menuItem : menuList) {
                menuListText.append(menuItem).append("\n");
            }
            textMenuList.setText(menuListText.toString());


        }
    }
}
