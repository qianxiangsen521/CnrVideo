package com.cnr.cnrvideo.cnrfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.cnr.cnrvideo.R;
import com.cnr.cnrvideo.bean.PlaySource;
import com.cnr.cnrvideo.event.PlaySourceEvent;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * 播放源fragment
 * 
 * @author xingzhiqiao 2015-10-29
 */
public class PlaySourceFragment extends Fragment implements OnClickListener {

	public List<PlaySource> mSources;
	private ListView mSourceListlv;
	private TextView mReloadTv;
	private LayoutInflater mInflater;
	private OnSelectSourceListener mOnSelectSourceListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_source, container,
				false);
		mInflater = LayoutInflater.from(getActivity());
		initView(view);
		return view;
	}

	private int selectIndex = 0;

	private ArrayAdapter<PlaySource> sourceAdapter;

	public void initView(View view) {
		mSourceListlv = (ListView) view.findViewById(R.id.play_source_list_lv);
		mSources = new ArrayList<PlaySource>();

		mReloadTv = (TextView) view.findViewById(R.id.player_reload_program_tv);
		mReloadTv.setOnClickListener(this);

		sourceAdapter = new ArrayAdapter<PlaySource>(getActivity(), -1,
				mSources) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = mInflater.inflate(
							R.layout.item_playsource_list, parent, false);
				}
				ImageView num = (ImageView) convertView
						.findViewById(R.id.play_source_num);
				TextView name = (TextView) convertView
						.findViewById(R.id.play_source_name);
				name.setText(mSources.get(position).getSourceName());
				if (selectIndex == position) {
					num.setImageResource(R.mipmap.bt_program_source_sel);
					name.setTextColor(getActivity().getResources().getColor(
							R.color.cnr_main_color));
				} else {
					num.setImageResource(R.mipmap.bt_program_source_unsel);
					name.setTextColor(getActivity().getResources().getColor(
							R.color.cnr_white));
				}
				return convertView;
			}
		};
		mSourceListlv.setAdapter(sourceAdapter);
		mSourceListlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				if (selectIndex != position) {
					selectIndex = position;
					sourceAdapter.notifyDataSetChanged();
					mOnSelectSourceListener.onSelectSource(
							mSources.get(position), position);
				}
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void setOnSelectSourceListener(
			OnSelectSourceListener onSelectSourceListener) {
		this.mOnSelectSourceListener = onSelectSourceListener;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEvent(PlaySourceEvent event) {
		mSources.clear();
		mSources.addAll(event.getPlaySources());
		sourceAdapter.notifyDataSetChanged();
	}

	public void onEvent(PlaySourceChangeEvent event) {
		selectIndex = event.selectIndex;
		sourceAdapter.notifyDataSetChanged();
	}

	public void notifyData(List<PlaySource> playSources, int selectedIndex) {
		mSources.clear();
		mSources.addAll(playSources);
		selectIndex = selectedIndex;
		sourceAdapter.notifyDataSetChanged();
	}

	/**
	 * 点击切换播放源接口
	 * 
	 * @author xingzhiqiao 2015-10-30
	 */
	public interface OnSelectSourceListener {
		void onSelectSource(PlaySource playSource, int selectedIndex);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.player_reload_program_tv:
			mOnSelectSourceListener.onSelectSource(null, 0);
			break;

		default:
			break;
		}
	}
}
