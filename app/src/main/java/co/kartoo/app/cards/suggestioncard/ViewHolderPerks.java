package co.kartoo.app.cards.suggestioncard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.Perks;

public class ViewHolderPerks extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView icon_perks;
    TextView txtHeaderPerks, txtTextPerks;
    Context mContext;
    Perks perks;


    public ViewHolderPerks(Context mContext, View itemView) {
        super(itemView);
        icon_perks = (ImageView) itemView.findViewById(R.id.icon_perks);
        txtHeaderPerks = (TextView) itemView.findViewById(R.id.txtHeaderPerks) ;
        txtTextPerks = (TextView) itemView.findViewById(R.id.txtTextPerks);

        itemView.setOnClickListener(this);

        this.mContext = mContext;
    }

    public void bind(Perks perks) {
        Log.e("TAG", "bind: "+perks.getHeader() );

        txtHeaderPerks.setText(perks.getHeader());
        txtTextPerks.setText(perks.getText());

        this.perks = perks;
    }

    @Override
    public void onClick(View v) {

    }
}