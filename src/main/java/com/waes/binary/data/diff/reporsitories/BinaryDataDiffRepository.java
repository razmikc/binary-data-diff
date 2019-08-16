package com.waes.binary.data.diff.reporsitories;

import com.waes.binary.data.diff.models.BinaryData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryDataDiffRepository extends CrudRepository<BinaryData, Long> {

}
