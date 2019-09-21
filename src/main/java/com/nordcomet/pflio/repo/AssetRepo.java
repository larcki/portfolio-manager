package com.nordcomet.pflio.repo;

import com.nordcomet.pflio.model.Asset;
import com.nordcomet.pflio.model.Tag;
import com.nordcomet.pflio.model.Tags;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AssetRepo extends CrudRepository<Asset, Integer> {

    List<Asset> findAssetsByTagsNameIn(List<Tags> tags);

//    List<Asset> findAssetsByCodeGro(List<Tags> tags);

}
