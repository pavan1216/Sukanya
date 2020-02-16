package f.com.livessavers.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import f.com.livessavers.Activitys.BloodSearchAcitivity;
import f.com.livessavers.Activitys.DonorInformartionActivity;
import f.com.livessavers.Interfaces.ItemClickListener;
import f.com.livessavers.Pojo.BloodGroupSpinner;
import f.com.livessavers.Pojo.DonorsListPojo;
import f.com.livessavers.R;

public class DonorsListAdapter extends RecyclerView.Adapter<DonorsListAdapter.MyViewHolder> {
        public List<DonorsListPojo> bloodgroups;
    public Context context;

    public DonorsListAdapter(Context context, List<DonorsListPojo> bloodgroups)
    {
        this.context=context;
        this.bloodgroups=bloodgroups;
    }


    @Override
    public DonorsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.donorslistadapter, parent, false);

        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener{


        public TextView donorname,donorgroup,donorcontact;

        public  ImageView bloodimage;


        private ItemClickListener clickListener;


        public MyViewHolder(View view) {
            super(view);

            donorname=(TextView)view.findViewById(R.id.blooddonorname);
            donorgroup=(TextView)view.findViewById(R.id.blooddonorgroup);
            donorcontact=(TextView)view.findViewById(R.id.blooddonorcontact);

            bloodimage=(ImageView)view.findViewById(R.id.donorbloodimages);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View view) {

            clickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {

            clickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;

        }
    }
    @Override
    public void onBindViewHolder(DonorsListAdapter.MyViewHolder holder, final int position) {
        holder.donorname.setText(bloodgroups.get(position).getDonorName());
        holder.donorgroup.setText("Blood Group : "+bloodgroups.get(position).getBloodGroupName());
        holder.donorcontact.setText(bloodgroups.get(position).getContactNumber().replaceAll("\\w(?=\\w{4})", "*"));

        holder.setClickListener(new ItemClickListener()
        {

            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                Intent productIntent = new Intent(context, DonorInformartionActivity.class);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                String stringObjectRepresentation = gson.toJson(bloodgroups.get(position));
                productIntent.putExtra("DonorInformation", stringObjectRepresentation);
                context.startActivity(productIntent);






            }
        });

    }

    @Override
    public int getItemCount() {
        return bloodgroups.size() ;
    }
}
