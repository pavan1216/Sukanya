package f.com.livessavers.Adapter;

import android.content.Context;
import android.content.Intent;
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
import f.com.livessavers.Interfaces.ItemClickListener;
import f.com.livessavers.Pojo.BloodGroupSpinner;
import f.com.livessavers.R;

public class BloodGroupAdapter  extends RecyclerView.Adapter<BloodGroupAdapter.MyViewHolder> {
        public List<BloodGroupSpinner> bloodgroups;
    public Context context;

    public BloodGroupAdapter(Context context, List<BloodGroupSpinner> bloodgroups)
    {
        this.context=context;
        this.bloodgroups=bloodgroups;
    }


    @Override
    public BloodGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloodgroupadapter, parent, false);

        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener{


        public TextView bloodgroupsnames;

        public  ImageView bloodimage;


        private ItemClickListener clickListener;


        public MyViewHolder(View view) {
            super(view);






            bloodgroupsnames=(TextView)view.findViewById(R.id.bloodgroupname);


            bloodimage=(ImageView)view.findViewById(R.id.bloodimages);






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
    public void onBindViewHolder(BloodGroupAdapter.MyViewHolder holder, final int position) {




            if (bloodgroups.get(position).getId()!=0)
            {
                holder.bloodgroupsnames.setText("Group " +bloodgroups.get(position).getBloodGroupName());

                holder.bloodimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent productIntent = new Intent(context, BloodSearchAcitivity.class);

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        String stringObjectRepresentation = gson.toJson(bloodgroups.get(position));
                        productIntent.putExtra("bloodgroup", stringObjectRepresentation);
                        context.startActivity(productIntent);

                    }
                });
            }
            else
            {

            }





















    }

    @Override
    public int getItemCount() {
        return bloodgroups.size() ;
    }
}
