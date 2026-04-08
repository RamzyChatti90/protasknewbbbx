export interface ICategory {
  id: number;
  name?: string | null;
  color?: string | null;
  icon?: string | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
