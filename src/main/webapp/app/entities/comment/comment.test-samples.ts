import dayjs from 'dayjs/esm';

import { IComment, NewComment } from './comment.model';

export const sampleWithRequiredData: IComment = {
  id: 20452,
};

export const sampleWithPartialData: IComment = {
  id: 5211,
  author: 'lest department pale',
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IComment = {
  id: 28427,
  author: 'testing ugh',
  content: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2026-04-08T07:30'),
};

export const sampleWithNewData: NewComment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
