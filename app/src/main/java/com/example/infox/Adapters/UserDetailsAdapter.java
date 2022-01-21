package com.example.infox.Adapters;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infox.Contacts;
import com.example.infox.DatabaseHelper;
import com.example.infox.R;

import java.util.List;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.UserViewHolder>{

    private List<Contacts> mcontacts;
    private Context context;
    private DatabaseHelper mydb;

    public UserDetailsAdapter(List<Contacts> mcontacts, Context context) {
        this.mcontacts = mcontacts;
        this.context = context;
        mydb = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Contacts contacts = mcontacts.get(position);

        //holder.iditem.setText(contacts.getId());
        holder.nameitem.setText(contacts.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                show_Dialog(view,position);

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 =  LayoutInflater.from(context).inflate(R.layout.update_user_details, (ViewGroup) view.getParent(), false);
                EditText name = view1.findViewById(R.id.name);
                EditText contactno = view1.findViewById(R.id.contactno);
                EditText emailid = view1.findViewById(R.id.emailid);
                Button cancel = view1.findViewById(R.id.cancel);
                Button update = view1.findViewById(R.id.update);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view1);
                builder.setCancelable(false);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                name.setText(mcontacts.get(position).getName());
                contactno.setText(mcontacts.get(position).getContactno());
                emailid.setText(mcontacts.get(position).getEmailid());

                update_data(update,alertDialog,name,contactno,emailid,holder,position);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                delete_data(mcontacts.get(position).getId(),position);
            }
        });
    }

    private void show_Dialog(View view, int position) {

        View view1 =  LayoutInflater.from(context).inflate(R.layout.show_items_layout, (ViewGroup) view.getParent(), false);
        TextView showname = view1.findViewById(R.id.showname);
        TextView showcontactno = view1.findViewById(R.id.showcontactno);
        TextView showemailid = view1.findViewById(R.id.showemailid);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view1);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        String pname = "Name : "+mcontacts.get(position).getName();
        String pcno = "ContactNo : "+mcontacts.get(position).getName();
        String pemail = "EmailID : "+mcontacts.get(position).getName();


        showname.setText(getcoloredtext(pname,7));
        showcontactno.setText(getcoloredtext(pcno,12));
        showemailid.setText(getcoloredtext(pemail,10));
    }

    private Spannable getcoloredtext(String pname, int i)
    {
        Spannable spannable = new SpannableString(pname);
        spannable.setSpan(new ForegroundColorSpan(Color.BLACK),0,i,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new android.text.style.StyleSpan(Typeface.BOLD),0,i,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private void delete_data(String iditem, int position)
    {
        int res=mydb.deletedata(iditem);
        if(res>0)
        {
            mcontacts.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "data not deleted", Toast.LENGTH_SHORT).show();
        }
    }

    private void update_data(Button update, AlertDialog alertDialog, EditText name, EditText contactno, EditText emailid, UserViewHolder holder, int position) {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean res= mydb.updatedata(mcontacts.get(position).getId(),name.getText().toString(),contactno.getText().toString(),emailid.getText().toString());

                if(res)
                {
                    mcontacts.get(position).setName(name.getText().toString());
                    mcontacts.get(position).setContactno(contactno.getText().toString());
                    mcontacts.get(position).setEmailid(emailid.getText().toString());

                    Toast.makeText(context, "data updated", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(context, "data not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mcontacts.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameitem;
        ImageButton edit,delete;

        public UserViewHolder(View itemView)
        {
            super(itemView);
            nameitem = (TextView)itemView.findViewById(R.id.nameitem);
            edit = (ImageButton) itemView.findViewById(R.id.edit);
            delete = (ImageButton) itemView.findViewById(R.id.delete);
        }
    }
}
