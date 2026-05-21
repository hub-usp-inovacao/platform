import { GlobalConfig } from 'payload';

export const HomeSettings: GlobalConfig = {
  slug: 'home-settings',
  label: 'Home Settings',
  access: {
    read: () => true,
  },
  fields: [
    {
      name: 'featuredPosts',
      label: 'Featured Posts',
      type: 'relationship',
      relationTo: 'posts',
      hasMany: true,
      admin: {
        description: 'Selecione os posts que aparecerão na página principal.',
      },
    },
  ],
};

