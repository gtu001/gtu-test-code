package gtu.mybatis.example.mapper;

import gtu.mybatis.example.domain.Sequence;

public interface SequenceMapper {

    Sequence getSequence(Sequence sequence);

    void updateSequence(Sequence sequence);
}
